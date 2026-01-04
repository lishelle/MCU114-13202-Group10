package com.example.tarotcards1

object TarotDailyAdvice {

    data class Advice(
        val name: String,
        val upright: String,
        val reversed: String
    )

    private val cards = listOf(

        // ===== Major Arcana =====
        Advice("The Fool", "勇敢嘗試新事物。", "先確認方向再前進。"),
        Advice("The Magician", "主動運用你的能力。", "專注在一件事上。"),
        Advice("The High Priestess", "相信直覺。", "留意被忽略的感受。"),
        Advice("The Empress", "溫柔對待自己與他人。", "顧好自己的需求。"),
        Advice("The Emperor", "建立清楚界線。", "放下過度控制。"),
        Advice("The Hierophant", "參考前人經驗。", "嘗試新做法。"),
        Advice("The Lovers", "忠於內心選擇。", "別勉強自己。"),
        Advice("The Chariot", "果斷行動。", "先調整方向。"),
        Advice("Strength", "用耐心面對問題。", "別逞強。"),
        Advice("The Hermit", "整理內心。", "適度與人交流。"),
        Advice("Wheel of Fortune", "順勢而行。", "接受當下節奏。"),
        Advice("Justice", "誠實面對選擇。", "別急著下結論。"),
        Advice("The Hanged Man", "換個角度看事。", "停止空等。"),
        Advice("Death", "接受改變。", "別再拖延。"),
        Advice("Temperance", "慢慢來。", "注意過度行為。"),
        Advice("The Devil", "看清依附。", "放下一個執著。"),
        Advice("The Tower", "接受重建。", "別再逃避。"),
        Advice("The Star", "保持希望。", "照顧情緒。"),
        Advice("The Moon", "多觀察。", "相信清晰感。"),
        Advice("The Sun", "主動展現正向。", "慢慢恢復能量。"),
        Advice("Judgement", "做出新決定。", "誠實面對自己。"),
        Advice("The World", "好好收尾。", "補齊最後一步。"),

        // ===== Wands =====
        Advice("Ace of Wands", "今天適合開始新事情。", "先找回動力。"),
        Advice("Two of Wands", "為未來做規劃。", "別再拖延選擇。"),
        Advice("Three of Wands", "耐心等待成果。", "重新檢視方向。"),
        Advice("Four of Wands", "享受穩定狀態。", "檢視安全感來源。"),
        Advice("Five of Wands", "從衝突中學習。", "避免無謂爭執。"),
        Advice("Six of Wands", "接受肯定。", "別否定自己。"),
        Advice("Seven of Wands", "為自己發聲。", "不必每場戰都參與。"),
        Advice("Eight of Wands", "把握進展速度。", "檢查溝通狀況。"),
        Advice("Nine of Wands", "撐一下但照顧自己。", "放下防備。"),
        Advice("Ten of Wands", "學會分擔。", "放下多餘責任。"),
        Advice("Page of Wands", "勇敢嘗試。", "別太衝動。"),
        Advice("Knight of Wands", "行動前先思考。", "放慢腳步。"),
        Advice("Queen of Wands", "相信自己。", "穩定情緒。"),
        Advice("King of Wands", "帶領而非控制。", "多傾聽他人。"),

        // ===== Cups =====
        Advice("Ace of Cups", "敞開感受。", "別壓抑情緒。"),
        Advice("Two of Cups", "珍惜互動。", "檢視關係平衡。"),
        Advice("Three of Cups", "與人分享喜悅。", "別過度依賴。"),
        Advice("Four of Cups", "檢視內心需求。", "把握新機會。"),
        Advice("Five of Cups", "允許難過。", "慢慢向前。"),
        Advice("Six of Cups", "珍惜回憶。", "活在當下。"),
        Advice("Seven of Cups", "別被幻想迷惑。", "做出決定。"),
        Advice("Eight of Cups", "勇敢轉身。", "誠實面對不捨。"),
        Advice("Nine of Cups", "感謝當下。", "檢視真正需求。"),
        Advice("Ten of Cups", "珍惜連結。", "接受不完美。"),
        Advice("Page of Cups", "溫柔回應。", "穩定情緒。"),
        Advice("Knight of Cups", "理想也要落地。", "面對現實。"),
        Advice("Queen of Cups", "照顧他人也照顧自己。", "保持界線。"),
        Advice("King of Cups", "穩定回應情緒。", "別冷處理感受。"),

        // ===== Swords =====
        Advice("Ace of Swords", "把話說清楚。", "避免倉促下結論。"),
        Advice("Two of Swords", "先冷靜觀望。", "面對現實。"),
        Advice("Three of Swords", "允許自己難過。", "慢慢放下。"),
        Advice("Four of Swords", "好好休息。", "別再逼自己。"),
        Advice("Five of Swords", "想清楚是否值得。", "退一步。"),
        Advice("Six of Swords", "慢慢離開困境。", "別再回頭糾結。"),
        Advice("Seven of Swords", "誠實比聰明重要。", "面對真相。"),
        Advice("Eight of Swords", "別小看可能性。", "跨出一步。"),
        Advice("Nine of Swords", "別讓擔心佔滿一天。", "試著放鬆。"),
        Advice("Ten of Swords", "接受結束。", "慢慢復原。"),
        Advice("Page of Swords", "保持學習心態。", "說話前三思。"),
        Advice("Knight of Swords", "確認方向再行動。", "放慢節奏。"),
        Advice("Queen of Swords", "理性判斷。", "多一點柔軟。"),
        Advice("King of Swords", "公平處理事情。", "傾聽不同聲音。"),

        // ===== Pentacles =====
        Advice("Ace of Pentacles", "把握眼前機會。", "檢查是否錯過什麼。"),
        Advice("Two of Pentacles", "調整生活節奏。", "減少負擔。"),
        Advice("Three of Pentacles", "重視合作。", "重新分工。"),
        Advice("Four of Pentacles", "保護重要事物。", "別太執著。"),
        Advice("Five of Pentacles", "尋求協助。", "慢慢回穩。"),
        Advice("Six of Pentacles", "保持付出平衡。", "別勉強給予。"),
        Advice("Seven of Pentacles", "給事情時間。", "別急著放棄。"),
        Advice("Eight of Pentacles", "專心做好一件事。", "注意疲勞。"),
        Advice("Nine of Pentacles", "享受成果。", "培養獨立。"),
        Advice("Ten of Pentacles", "重視長期規劃。", "檢視基礎。"),
        Advice("Page of Pentacles", "一步一步來。", "補好計畫。"),
        Advice("Knight of Pentacles", "持之以恆。", "別停在原地。"),
        Advice("Queen of Pentacles", "照顧生活細節。", "別忘了自己。"),
        Advice("King of Pentacles", "穩定做決定。", "別只看結果。")
    )

    fun getAdvice(name: String, reversed: Boolean): String {
        val card = cards.find { it.name == name }
        return if (reversed) {
            card?.reversed ?: "今天放慢腳步即可。"
        } else {
            card?.upright ?: "今天保持穩定即可。"
        }
    }
}
