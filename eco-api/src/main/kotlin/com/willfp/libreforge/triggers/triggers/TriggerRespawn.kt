package com.willfp.libreforge.triggers.triggers

import com.willfp.eco.core.integrations.mcmmo.McmmoManager
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerRespawnEvent

class TriggerRespawn : Trigger(
    "respawn", listOf(
        TriggerParameter.PLAYER,
        TriggerParameter.LOCATION
    )
) {
    @EventHandler(ignoreCancelled = true)
    fun handle(event: PlayerRespawnEvent) {
        if (McmmoManager.isFake(event)) {
            return
        }

        this.processTrigger(
            event.player,
            TriggerData(
                player = event.player,
                location = event.player.location
            )
        )
    }
}
