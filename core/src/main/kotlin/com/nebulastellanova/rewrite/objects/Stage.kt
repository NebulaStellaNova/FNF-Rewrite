package com.nebulastellanova.rewrite.objects

import org.flixelgdx.group.FlixelSpriteGroup

class Stage : FlixelSpriteGroup {

    var id: String

    constructor(id: String) : super() {
        this.id = id
    }

}
