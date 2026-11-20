/* Zippy_Cut_Pro(II) — 🔐 the MASTER PROMPT (0.9.2) and the ANSWER READER (0.9.3) live on the server now
   (apps_script/Prompt.gs · ZP.zippyPrompt / ZP.zippyRead), never in the app. A cracked APK has neither: it cannot get a
   prompt, and even with a prompt copied from somebody's PDF it cannot turn the AI's answer into a recap.
   Only the length ratio (not secret) is left here. */
(function(){
'use strict';
const MP_RATIO=0.7;
window.StoryCore={ratio:style=>style==='dub'?0.95:style==='dialogue'?0.65:MP_RATIO};
})();
