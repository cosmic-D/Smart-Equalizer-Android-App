package com.smarteq.models

data class AudioLabel(
    val name: String,
    val score: Float
) {
    companion object {
        // YAMNet labels for voice detection
        val VOICE_LABELS = setOf(
            "Speech",
            "Conversation",
            "Narration, monologue",
            "Babbling",
            "Speech synthesizer",
            "Shout",
            "Bellow",
            "Whoop",
            "Yell",
            "Children shouting",
            "Screaming",
            "Whispering",
            "Laughter",
            "Baby laughter",
            "Giggle",
            "Snicker",
            "Belly laugh",
            "Chuckle, chortle"
        )
        
        // YAMNet labels for music detection
        val MUSIC_LABELS = setOf(
            "Music",
            "Musical instrument",
            "Instrumental music",
            "Singing",
            "Choir",
            "Yodeling",
            "Chant",
            "Chorus",
            "Rap",
            "Beatboxing",
            "Harmonica",
            "Accordion",
            "Bagpipes",
            "Banjo",
            "Bass guitar",
            "Cello",
            "Clarinet",
            "Double bass",
            "Drum kit",
            "Electric guitar",
            "Flute",
            "French horn",
            "Guitar",
            "Harp",
            "Harpsichord",
            "Keyboard (musical)",
            "Mandolin",
            "Marimba, xylophone",
            "Oboe",
            "Organ",
            "Percussion",
            "Piano",
            "Saxophone",
            "Sitar",
            "Snare drum",
            "Synthesizer",
            "Tambourine",
            "Trombone",
            "Trumpet",
            "Tuba",
            "Ukulele",
            "Violin, fiddle",
            "Zither"
        )
        
        // YAMNet labels for advertisement detection
        val ADVERTISEMENT_LABELS = setOf(
            "Television",
            "Radio",
            "Commercial",
            "Advertisement",
            "Jingle",
            "Announcement"
        )
    }
} 