package com.example.cleanaf.util

data class Reward(val name: String, val threshold: Int, val unlocked: Boolean)

object RewardManager {
    private val thresholds = listOf(
        Reward("Starter", 10, false),
        Reward("Task Tamer", 50, false),
        Reward("Cleaning Pro", 100, false),
        Reward("Habit Hero", 200, false),
        Reward("Routine Machine", 300, false),
        Reward("Dust Slayer", 400, false),
        Reward("Productivity Beast", 500, false),
        Reward("Shiny Soul", 650, false),
        Reward("Efficiency Wizard", 800, false),
        Reward("Zen Master", 1000, false),
        Reward("Chaos Destroyer", 1250, false),
        Reward("Ultra Cleaner", 1500, false),
        Reward("Supreme Overcleaner", 2000, false)
    )

    fun getUnlockedRewards(currentPoints: Int): List<Reward> {
        return thresholds.map {
            it.copy(unlocked = currentPoints >= it.threshold)
        }
    }
}
