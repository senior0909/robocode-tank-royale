package dev.robocode.tankroyale.gui.ui.menu

import dev.robocode.tankroyale.gui.ui.menu.MenuEventTriggers.onBotDirConfig
import dev.robocode.tankroyale.gui.ui.menu.MenuEventTriggers.onDebugConfig
import dev.robocode.tankroyale.gui.ui.menu.MenuEventTriggers.onServerConfig
import dev.robocode.tankroyale.gui.ui.menu.MenuEventTriggers.onSetupRules
import dev.robocode.tankroyale.gui.ui.menu.MenuEventTriggers.onShowServerLog
import dev.robocode.tankroyale.gui.ui.menu.MenuEventTriggers.onStartBattle
import dev.robocode.tankroyale.gui.server.ServerProcess
import dev.robocode.tankroyale.gui.ui.MenuTitles
import dev.robocode.tankroyale.gui.ui.extensions.JMenuExt.addNewMenuItem
import dev.robocode.tankroyale.gui.ui.menu.MenuEventTriggers.onAbout
import dev.robocode.tankroyale.gui.ui.menu.MenuEventTriggers.onRebootServer
import dev.robocode.tankroyale.gui.ui.menu.MenuEventTriggers.onStartServer
import dev.robocode.tankroyale.gui.ui.menu.MenuEventTriggers.onStopServer
import dev.robocode.tankroyale.gui.ui.server.Server
import dev.robocode.tankroyale.gui.ui.server.ServerEvents
import java.awt.event.KeyEvent
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.KeyStroke

object Menu : JMenuBar() {

    private lateinit var startServerMenuItem: JMenuItem
    private lateinit var rebootServerMenuItem: JMenuItem
    private lateinit var stopServerMenuItem: JMenuItem

    init {
        MenuEventHandlers

        setupBattleMenu()
        setupServerMenu()
        setupConfigMenu()
        setupHelpMenu()

        ServerEvents.apply {
            onStarted.subscribe(Menu) { updateServerState() }
            onStopped.subscribe(Menu) { updateServerState() }
        }
    }

    private fun setupBattleMenu() {
        add(JMenu(MenuTitles.get("menu.battle")).apply {
            mnemonic = KeyEvent.VK_B

            addNewMenuItem("item.start_battle", onStartBattle).apply {
                mnemonic = KeyEvent.VK_B
                accelerator = ctrlDown(mnemonic)
            }
            addSeparator()
            addNewMenuItem("item.setup_rules", onSetupRules).apply {
                mnemonic = KeyEvent.VK_R
                accelerator = ctrlDown(mnemonic)
            }
        })
    }

    private fun setupServerMenu() {
        val serverMenu = JMenu(MenuTitles.get("menu.server")).apply {
            mnemonic = KeyEvent.VK_S

            startServerMenuItem = addNewMenuItem("item.start_server", onStartServer)
            stopServerMenuItem = addNewMenuItem("item.stop_server", onStopServer)
            rebootServerMenuItem = addNewMenuItem("item.reboot_server", onRebootServer)

            addNewMenuItem("item.show_server_log", onShowServerLog).apply {
                mnemonic = KeyEvent.VK_L
                accelerator = ctrlDown(mnemonic)
            }
            addSeparator()
            addNewMenuItem("item.select_server", onServerConfig).apply {
                mnemonic = KeyEvent.VK_E
            }
            addSeparator()

            add(startServerMenuItem).apply {
                mnemonic = KeyEvent.VK_S
                accelerator = ctrlDown(mnemonic)
            }
            add(rebootServerMenuItem).apply {
                mnemonic = KeyEvent.VK_R
            }
            add(stopServerMenuItem).apply {
                mnemonic = KeyEvent.VK_T
            }

            updateServerState()
        }
        add(serverMenu)
    }

    private fun setupConfigMenu() {
        add(JMenu(MenuTitles.get("menu.config")).apply {
            mnemonic = KeyEvent.VK_C

            addNewMenuItem("item.bot_root_dirs_config", onBotDirConfig).apply {
                mnemonic = KeyEvent.VK_D
                accelerator = ctrlDown(mnemonic)
            }

            addNewMenuItem("item.debug_config", onDebugConfig).apply {
                mnemonic = KeyEvent.VK_C
            }
        })
    }

    private fun setupHelpMenu() {
        add(JMenu(MenuTitles.get("menu.help")).apply {
            mnemonic = KeyEvent.VK_H

            addNewMenuItem("item.about", onAbout).apply {
                mnemonic = KeyEvent.VK_A
            }
        })
    }

    private fun updateServerState() {
        startServerMenuItem.isEnabled = !Server.isRunning()
        rebootServerMenuItem.isEnabled = ServerProcess.isRunning()
        stopServerMenuItem.isEnabled = ServerProcess.isRunning()
    }

    private fun ctrlDown(keyEvent: Int) = KeyStroke.getKeyStroke(keyEvent, KeyEvent.CTRL_DOWN_MASK)
}