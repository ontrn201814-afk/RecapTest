/* 🎬 Long video (One Click) — 0.3.0: the AI gets a PDF + an MP3 instead of a video (Gemini can't open a zip · far fewer tokens)
   · PDF = the master prompt again · a table of the talking pieces · frame cards (one picture every 4 s, movie time on each,
     🔊 cyan = people talk (+ where it is in the MP3) · 🔇 grey = nobody talks · ✂ = a new camera shot)
   · MP3 = only the talking (SpeechVad on the phone), piece after piece, 1.25× fast · its map says where each piece came from
   The AI answers in 🅱's own [N] / [D mm:ss-mm:ss] tags — the [D] time is MP3 time → movie time here (map) → on the nearest real
   start of talking (onsets, ±1 s). The [N] lines get 🅱's even spread between the [D] lines (one smooth run of the movie).
   The JSON reader of 0.1 / 0.2 stays (📂 an old .txt still opens). */
(function(){
'use strict';
const LONG_MIN = 30, LONG_SPEED = 1;      // 0.5.2 30′ parts · no 2× any more (no video goes to the AI) — times are the part's real clock
const LONG_RATE = 20, LONG_LEN10 = 7, LONG_BAND = 0.1;   // 🗣 TTS 1.2× (+20 %) · 0.5.2 output 60–80 % of the movie (aim 70 %)
const STEP = 4, AUDIO = 1.25;             // a picture every 4 s · the MP3 1.25× fast (defaults · ⚙ in the One Click screen)
const COLS = 4, ROWS = 5;                 // 20 pictures on a card page
const VERSION = 'long-v1';
const toA = s => String(s).replace(/[၀-၉]/g, d => String(d.charCodeAt(0) - 0x1040));
const hms = s => { s = Math.max(0, Math.round(s)); const h = Math.floor(s / 3600), m = Math.floor(s % 3600 / 60), x = s % 60; return [h, m, x].map(v => String(v).padStart(2, '0')).join(':'); };
/** part clock mm:ss (h:mm:ss past an hour) */
const r3 = v => Math.round(v * 1000) / 1000;
const mmss = s => { s = Math.max(0, Math.round(s)); return s >= 3600 ? hms(s) : String(Math.floor(s / 60)).padStart(2, '0') + ':' + String(s % 60).padStart(2, '0'); };

/* ───────── ① the MP3 map: [r0, r1, m0, m1] = movie (part) time ↔ MP3 time of one talking piece ───────── */
/** MP3 time → part time (inside a piece: by the speed · past the end: the last piece's end) */
function mp3ToReal(map, m) {
  if (!map || !map.length || !isFinite(m)) return NaN;
  for (let j = 0; j < map.length; j++) {                 // a time on the joint belongs to the piece that starts there
    const [r0, r1, m0, m1] = map[j];
    if (m < m1 - 1e-6 || j === map.length - 1) { const k = m1 > m0 ? (r1 - r0) / (m1 - m0) : 1; return r3(Math.max(r0, Math.min(r1, r0 + (Math.max(m, m0) - m0) * k))); }
  }
  return map[map.length - 1][1];
}
/** part time → MP3 time (null = nobody talks there) */
function realToMp3(map, r) {
  for (const [r0, r1, m0, m1] of map || []) if (r >= r0 - 1e-6 && r <= r1 + 1e-6) return r3(m0 + (r - r0) * (m1 - m0) / Math.max(1e-6, r1 - r0));
  return null;
}
/** the talking inside [a, b) of the part → {r: first talking second, m: its MP3 time} or null */
function talkIn(map, a, b) {
  for (const [r0, r1, m0, m1] of map || []) if (r0 < b && r1 > a) { const r = Math.max(a, r0); return { r, m: r3(m0 + (r - r0) * (m1 - m0) / Math.max(1e-6, r1 - r0)) }; }
  return null;
}
/** the nearest real start of talking within ±tol s, later than [after] (none → t) */
function snap(t, onsets, tol, after) {
  tol = tol == null ? 1.0 : tol; after = after == null ? -Infinity : after; let best = t, d = tol + 1e-9;
  for (const o of onsets || []) { const x = Math.abs(o - t); if (o > after && x < d) { d = x; best = o; } }
  return best;
}
/** the AI's script lines ([D] in MP3 time) → part time · {segs, moved, snapped, dropped}
 *  In the speech-only MP3 two seconds side by side can be minutes apart in the movie (a joint between pieces), and the AI
 *  writes whole seconds (± its own error). So every [D] goes on a real START of talking (onsets) chosen IN MP3 TIME, all
 *  the lines together in story order (the cheapest path: MP3 seconds off + the story's own place in the part), and only
 *  then becomes movie time. A line with no start of talking near it keeps its own MP3 time (map); one that would go back
 *  in the movie loses its time (🅱 spreads it between its neighbours, still the character's line · 🎯 to fix by hand). */
function mapPlan(segs, pack) {
  const map = pack && pack.map;
  let moved = 0, snapped = 0, dropped = 0, last = -Infinity;
  if (!map || !map.length) return { segs, moved, snapped, dropped };
  const on = (pack.onsets || []).map(r => ({ r, m: realToMp3(map, r) })).filter(o => o.m != null).sort((x, y) => x.r - y.r);
  // where in the movie a line should be, from where it is in the story (its share of the words so far) — the AI tells the
  // story in order and evenly (🅱's part groups), so this is right to a minute or two: enough to tell two pieces apart
  // that sit side by side in the MP3 but minutes apart in the movie
  const dur = +pack.dur || map[map.length - 1][1], w = segs.map(x => Math.max(1, String(x.text || '').length)), W = w.reduce((a, b) => a + b, 0);
  let acc = 0; const E = w.map(v => { const e = (acc + v / 2) / W * dur; acc += v; return e; });
  const Di = segs.map((x, n) => x.type === 'D' && x.s0 != null ? n : -1).filter(n => n >= 0);
  // all the [D] lines at once, in story order, each on its own start of talking (or none): the cheapest way through —
  // cost = MP3 seconds off (≤ 4) + a minute off the story's place = 1 s (at most 2) · a line left without one costs 3.5
  const nD = Di.length, m = on.length, SKIP = 3.5, INF = 1e9;
  const cost = (i, j) => { const x = segs[Di[i]], k = Math.abs(on[j].m - x.s0); return k > 4 ? INF : k + Math.min(2, Math.abs(on[j].r - E[Di[i]]) / 60); };
  let prev = new Float64Array(m + 1).fill(INF); prev[0] = 0;                   // state k = last onset used + 1 (0 = none yet)
  const from = [];
  for (let i = 0; i < nD; i++) {
    const cur = new Float64Array(m + 1).fill(INF), how = new Int32Array(m + 1).fill(-1);    // -1 = skipped (kept k) · else came from state
    for (let k = 0; k <= m; k++) if (prev[k] + SKIP < cur[k]) { cur[k] = prev[k] + SKIP; how[k] = -1; }
    let bk = 0, bv = prev[0];
    for (let j = 0; j < m; j++) {                                               // assign onset j (state j+1) after any state ≤ j
      if (prev[j] < bv) { bv = prev[j]; bk = j; }
      const c = cost(i, j); if (c >= INF || bv >= INF) continue;
      if (bv + c < cur[j + 1]) { cur[j + 1] = bv + c; how[j + 1] = bk; }
    }
    from.push(how); prev = cur;
  }
  const pick = new Array(nD).fill(-1);
  let k = 0, bv = INF; for (let q = 0; q <= m; q++) if (prev[q] < bv) { bv = prev[q]; k = q; }
  for (let i = nD - 1; i >= 0; i--) { const h = from[i][k]; if (h === -1) pick[i] = -1; else { pick[i] = k - 1; k = h; } }
  const at = new Map(Di.map((n, i) => [n, pick[i]]));
  const out = segs.map((x, n) => {
    if (x.type !== 'D' || x.s0 == null) return x;
    const len = Math.max(0.6, Math.min(12, ((x.s1 != null ? x.s1 : x.s0 + 1.6) - x.s0) * (pack.speed || AUDIO)));
    const j = at.get(n);
    let a = j >= 0 ? on[j].r : mp3ToReal(map, x.s0);
    if (j >= 0) snapped++;
    if (!(a > last - 0.6)) { dropped++; return Object.assign({}, x, { s0: null, s1: null, mp3: [x.s0, x.s1] }); }
    a = Math.max(a, last); last = a + len * 0.5; moved++;
    return Object.assign({}, x, { s0: +a.toFixed(3), s1: +(a + len).toFixed(3), mp3: [x.s0, x.s1] });
  });
  return { segs: out, moved, snapped, dropped };
}

/* ───────── ② what goes into the PDF ───────── */
/** frame cards: [{k, a, b, cells: [{name, t, talk: {r, m} | null, cut}]}] — part time · [shots] in part time */
function cards(pack, shots) {
  const step = pack.step || STEP, per = COLS * ROWS, fr = pack.frames || [], sh = (shots || []).slice().sort((x, y) => x - y), out = [];
  for (let k = 0; k * per < fr.length; k++) {
    const cells = fr.slice(k * per, (k + 1) * per).map((name, j) => {
      // 0.3.2 the picture's own number says its time (a spot of the movie that would not decode = no picture there, the rest stay right)
      const m = /(\d+)\.jpg$/.exec(String(name)), n = m ? +m[1] : k * per + j, t = n * step, talk = talkIn(pack.map, t, t + step);
      // 📝 0.4.0 Whisper: the TALK lines heard in this picture's seconds (S numbers)
      const ids = hasLines(pack) ? pack.lines.map((l, q) => (l[0] < t + step && l[1] > t ? q + 1 : 0)).filter(Boolean) : null;
      return { name, t, talk: ids && !ids.length ? null : talk, ids, cut: sh.some(c => c > t - step + 0.01 && c <= t + 0.01 && t > 0) };
    });
    out.push({ k, a: cells[0].t, b: cells[cells.length - 1].t + step, cells });
  }
  return out;
}
/** the talking table: one row per piece */
function tableRows(map) {
  return (map || []).map(([r0, r1, m0, m1], k) => `S${String(k + 1).padStart(3, '0')}  MP3 ${mmss(m0)}–${mmss(m1)}  =  movie ${mmss(r0)}–${mmss(r1)}  (${(r1 - r0).toFixed(1)} s)`);
}
const PAGE_TOK = 258, AUDIO_TOK = 32;      // Gemini: a PDF page · a second of sound
/** 📝 0.4.0 the pack has Whisper's lines (no MP3) */
const hasLines = pk => !!(pk && Array.isArray(pk.lines) && pk.lines.length);
/** the TALK list: "S001 [01:23.4–01:27.0] text" (the part's clock — like the Kotlin AsrPlan.rows) */
const clk = s => { s = Math.max(0, +s || 0); const m = Math.floor(s / 60), x = Math.min(59.9, s - m * 60); return String(m).padStart(2, '0') + ':' + x.toFixed(1).padStart(4, '0'); };
function talkRows(lines) { return (lines || []).map((l, k) => `S${String(k + 1).padStart(3, '0')} [${clk(l[0])}–${clk(l[1])}] ${l[2]}`); }
/** text → Gemini tokens (about: CJK ~1 a letter · other ~4 letters a token) */
function textTok(t) { t = String(t || ''); const cjk = (t.match(/[\u3040-\u30ff\u3400-\u9fff\uac00-\ud7af]/g) || []).length; return Math.round(cjk * 0.9 + (t.length - cjk) / 4); }
function tokens(pack, promptPages) {
  const lines = hasLines(pack);
  const nCards = Math.ceil(((pack && pack.frames) || []).length / (COLS * ROWS)), nTable = lines ? 0 : Math.ceil(((pack && pack.map) || []).length / 120);
  const pages = (promptPages || 3) + nTable + nCards, sec = pack && pack.mp3 ? +pack.mp3.sec || 0 : 0;
  const text = lines ? textTok(talkRows(pack.lines).join('\n')) : 0;
  return { pages, pdf: pages * PAGE_TOK, mp3: Math.round(sec * AUDIO_TOK), text, total: pages * PAGE_TOK + Math.round(sec * AUDIO_TOK) + text };
}

/* ───────── ③ the prompt: 🅱's master prompt word for word + how the "video" comes (PDF + MP3) ───────── */
/** 🎬 0.6.0 where this part sits in the movie — the opening only in Part 1, the ending only in the last part, the parts
 *  between go straight on (o.tail = the last words of the part before) · o.each = 🧩 every part is watched on its own */
/* 🔐 placeRules / promptPdf / promptTalk moved to the server (apps_script/Prompt.gs) — the app holds no prompt text */
/* ───────── ② reading the AI's .txt ───────── */
/** looks like our JSON plan (not the old [N] / [D] tags) — only a hint for the paste box · the reading itself is on the server */
function looks(raw) { const s = String(raw || '').replace(/[“”]/g, '"'); return /[{[]/.test(s) && /"(lines|script|plan|items|segments|type|text)"\s*:/.test(s); }
/* 🔐 0.9.3 sec / range / grab / parse (the JSON answer reader) moved to the server (apps_script/Prompt.gs · ZR) */

window.LongCore = { MIN: LONG_MIN, SPEED: LONG_SPEED, RATE: LONG_RATE, LEN10: LONG_LEN10, BAND: LONG_BAND, STEP, AUDIO, COLS, ROWS, PAGE_TOK, AUDIO_TOK, VERSION,
  hasLines, talkRows, textTok, mp3ToReal, realToMp3, talkIn, snap, mapPlan, cards, tableRows, tokens, looks, hms, mmss };
})();
