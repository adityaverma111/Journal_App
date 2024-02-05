package com.example.journalapp

class Journal{
    var imageUri: String? = null
    var thoughts: String? = null
    var timestamp: com.google.firebase.Timestamp? = null
    var title: String? = null
    var userId: String? = null
    var userName: String? = null

    // No-argument constructor
    //constructor()

    // All-arguments constructor (if needed)
    constructor(title: String?, thoughts: String?, imageUri: String?, userId: String?, timestamp: com.google.firebase.Timestamp?, userName: String?) {
        this.title = title
        this.thoughts = thoughts
        this.imageUri = imageUri
        this.userId = userId
        this.timestamp = timestamp
        this.userName = userName
    }
    constructor(){

    }
}

