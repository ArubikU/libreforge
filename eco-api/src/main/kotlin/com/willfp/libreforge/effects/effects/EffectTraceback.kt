package com.willfp.libreforge.effects.effects

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.ConfigViolation
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.getDoubleFromExpression
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers
import org.bukkit.Bukkit
import org.bukkit.Location

class EffectTraceback : Effect(
    "traceback",
    triggers = Triggers.withParameters(
        TriggerParameter.PLAYER
    )
) {
    private val key = "${plugin.name}_traceback"

    override fun handle(data: TriggerData, config: Config) {
        val player = data.player ?: return

        val time = config.getDoubleFromExpression("seconds", data).toInt().coerceIn(1..30)

        @Suppress("UNCHECKED_CAST")
        val times = player.getMetadata(key).getOrNull(0)?.value() as? List<Location> ?: emptyList()

        // Most recent is last
        val index = times.size - time

        val location = times.getOrElse(index) { times.lastOrNull() } ?: return

        player.teleport(location)
    }

    override fun validateConfig(config: Config): List<ConfigViolation> {
        val violations = mutableListOf<ConfigViolation>()

        if (!config.has("seconds")) violations.add(
            ConfigViolation(
                "seconds",
                "You must specify the amount of seconds back in time (between 1 and 30)!"
            )
        )

        return violations
    }

    fun init() {
        plugin.scheduler.runTimer(20, 20) {
            for (player in Bukkit.getOnlinePlayers()) {
                @Suppress("UNCHECKED_CAST")
                val times = player.getMetadata(key).getOrNull(0)?.value() as? List<Location> ?: emptyList()
                val newTimes = (if (times.size < 29) times else times.drop(1)) + player.location

                player.removeMetadata(key, plugin)
                player.setMetadata(key, plugin.metadataValueFactory.create(newTimes))
            }
        }
    }
}
