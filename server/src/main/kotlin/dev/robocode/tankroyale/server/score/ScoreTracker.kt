package dev.robocode.tankroyale.server.score

import dev.robocode.tankroyale.server.model.BotId
import dev.robocode.tankroyale.server.model.Score
import dev.robocode.tankroyale.server.rules.*

/** Score utility class used for keeping track of the score for an individual bot in a game. */
class ScoreTracker(botIds: Set<BotId>) {

    /** Set of bot identifiers  */
    private val botIds: Set<BotId> = HashSet(botIds)

    /** Map from bot identifier to a bot record  */
    private val scoreAndDamages = mutableMapOf<BotId, ScoreAndDamage>()

    /** Set of identifiers of bots alive  */
    private val botsAliveIds = mutableSetOf<BotId>()

    /** 1st places  */
    private val firstPlaces = mutableMapOf<BotId, Int>()

    /** 2nd places  */
    private val secondPlaces = mutableMapOf<BotId, Int>()

    /** 3rd places  */
    private val thirdPlaces = mutableMapOf<BotId, Int>()

    init {
        initializeDamageAndSurvivals()
    }

    /** Current results ordered with higher total scores first. */
    val results: List<Score> get() = botScores

    /** Initializes the map containing the BotRecord record for each bot. */
    private fun initializeDamageAndSurvivals() {
        botIds.forEach { botId -> scoreAndDamages[botId] = ScoreAndDamage() }
    }

    /** Prepare for new round. */
    fun prepareRound() {
        botsAliveIds.clear()
        botsAliveIds += botIds
    }

    /** Current bot scores ordered with higher total scores first. */
    private val botScores: MutableList<Score>
        get() {
            val scores = mutableListOf<Score>()
            botIds.forEach { scores += getScore(it) }
            scores.sortByDescending { it.totalScore }
            return scores
        }

    /**
     * Returns the score for a specific bot.
     * @param botId is the identifier of the bot.
     * @return a score record.
     */
    private fun getScore(botId: BotId): Score {
        val damageRecord = scoreAndDamages[botId] ?: throw IllegalStateException("No score record for botId: $botId")
        damageRecord.apply {
            val score = Score(
                botId = botId,
                survival = survivalCount * SCORE_PER_SURVIVAL,
                lastSurvivorBonus = lastSurvivorCount * BONUS_PER_LAST_SURVIVOR,
                bulletDamage = totalBulletDamage * SCORE_PER_BULLET_DAMAGE,
                ramDamage = totalRamDamage * SCORE_PER_RAM_DAMAGE,
            )
            for (enemyId in getBulletKillEnemyIds()) {
                val totalDamage = getBulletDamage(enemyId) + getRamDamage(enemyId)
                score.bulletKillBonus += totalDamage * BONUS_PER_BULLET_KILL
            }
            for (enemyId in getRamKillEnemyIds()) {
                val totalDamage = getBulletDamage(enemyId) + getRamDamage(enemyId)
                score.ramKillBonus += totalDamage * BONUS_PER_RAM_KILL
            }
            score.firstPlaces = firstPlaces[botId] ?: 0
            score.secondPlaces = secondPlaces[botId] ?: 0
            score.thirdPlaces = thirdPlaces[botId] ?: 0
            return score
        }
    }

    /**
     * Registers a bullet hit.
     * @param botId is the identifier of the bot that hit another bot.
     * @param victimBotId is the identifier of the victim bot that got hit by the bullet.
     * @param damage is the damage that the victim bot receives.
     * @param kill is a flag specifying, if the bot got killed by this bullet.
     */
    fun registerBulletHit(botId: BotId, victimBotId: BotId, damage: Double, kill: Boolean) {
        val damageRecord = scoreAndDamages[botId] ?: throw IllegalStateException("No score record for botId: $botId")
        damageRecord.apply {
            addBulletDamage(victimBotId, damage)
            if (kill) {
                addBulletKillEnemyId(victimBotId)
            }
        }
    }

    /**
     * Registers a ram hit.
     * @param botId is the identifier of the bot that rammed another bot.
     * @param victimBotId is the identifier of the victim bot that got rammed.
     * @param kill is a flag specifying, if the bot got killed by the ramming.
     */
    fun registerRamHit(botId: BotId, victimBotId: BotId, kill: Boolean) {
        val damageRecord = scoreAndDamages[botId] ?: throw IllegalStateException("No score record for botId: $botId")
        damageRecord.apply {
            addRamDamage(victimBotId)
            if (kill) {
                addRamKillEnemyId(victimBotId)
            }
        }
    }

    /**
     * Register a bot death.
     * @param botId is the identifier of the bot that died.
     */
    fun registerBotDeath(botId: BotId) {
        botsAliveIds.apply {
            remove(botId)
            forEach { scoreAndDamages[it]?.incrementSurvivalCount() }
            if (size == 1) {
                val survivorId = botsAliveIds.first()
                val deadCount = scoreAndDamages.size - botsAliveIds.size
                scoreAndDamages[survivorId]?.addLastSurvivorCount(deadCount)
            }
        }
    }

    /**
     * Increment the number of 1st places for a bot.
     * @param botId is the identifier of the bot that earned a 1st place.
     */
    fun increment1stPlaces(botId: BotId) {
        val count = firstPlaces[botId] ?: 0
        firstPlaces[botId] = count + 1
    }

    /**
     * Increment the number of 2nd places for a bot.
     * @param botId is the identifier of the bot that earned a 2nd place.
     */
    fun increment2ndPlaces(botId: BotId) {
        val count = secondPlaces[botId] ?: 0
        secondPlaces[botId] = count + 1
    }

    /**
     * Increment the number of 3rd places for a bot.
     * @param botId is the identifier of the bot that earned a 3rd place.
     */
    fun increment3rdPlaces(botId: BotId) {
        val count = thirdPlaces[botId] ?: 0
        thirdPlaces[botId] = count + 1
    }
}