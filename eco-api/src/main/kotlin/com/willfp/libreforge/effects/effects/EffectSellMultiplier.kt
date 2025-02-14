package com.willfp.libreforge.effects.effects

import com.willfp.eco.core.integrations.shop.ShopSellEvent
import com.willfp.libreforge.effects.GenericMultiplierEffect
import org.bukkit.event.EventHandler

class EffectSellMultiplier : GenericMultiplierEffect("sell_multiplier") {
    @EventHandler
    fun handle(event: ShopSellEvent) {
        event.price *= getMultiplier(event.player)
    }
}
