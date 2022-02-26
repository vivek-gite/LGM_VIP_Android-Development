package com.lgm.covidTracker

data class Model(
    var city: String,
    var state:String,
    var active: String,
    var confirmed: String,
    var deceased: String,
    var recovered: String,
    var dconfirmed: String,
    var ddeceased: String,
    var drecovered: String
)