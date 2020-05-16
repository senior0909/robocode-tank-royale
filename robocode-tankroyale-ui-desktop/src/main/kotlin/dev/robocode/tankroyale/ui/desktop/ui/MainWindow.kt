package dev.robocode.tankroyale.ui.desktop.ui

import com.sun.jdi.Bootstrap
import dev.robocode.tankroyale.ui.desktop.bootstrap.BootstrapProcess
import dev.robocode.tankroyale.ui.desktop.client.Client
import dev.robocode.tankroyale.ui.desktop.ui.extensions.WindowExt.onClosing
import dev.robocode.tankroyale.ui.desktop.server.ServerProcess
import dev.robocode.tankroyale.ui.desktop.ui.arena.BattlePanel
import dev.robocode.tankroyale.ui.desktop.ui.arena.LogoPanel
import dev.robocode.tankroyale.ui.desktop.ui.selection.*
import dev.robocode.tankroyale.ui.desktop.ui.config.BotDirectoryConfigDialog
import dev.robocode.tankroyale.ui.desktop.ui.config.SetupRulesDialog
import dev.robocode.tankroyale.ui.desktop.ui.server.PrepareServerCommand
import dev.robocode.tankroyale.ui.desktop.ui.server.SelectServerDialog
import dev.robocode.tankroyale.ui.desktop.ui.server.ServerLogWindow
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import java.awt.EventQueue
import java.io.Closeable
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.UIManager


@UnstableDefault
@ImplicitReflectionSerializer
object MainWindow : JFrame(ResourceBundles.UI_TITLES.get("main_window")), AutoCloseable {

    init {
        defaultCloseOperation = EXIT_ON_CLOSE

        setSize(880, 760)
        setLocationRelativeTo(null) // center on screen

        contentPane.add(LogoPanel)

        jMenuBar = MainWindowMenu

        val iconUrl = javaClass.getResource("/gfx/Tank.png")
        val iconImage = ImageIcon(iconUrl)
        setIconImage(iconImage.image)


        MainWindowMenu.apply {
            onSelectBots.invokeLater { selectBots() }
            onSetupRules.invokeLater { SetupRulesDialog.isVisible = true }
            onShowServerLog.invokeLater { ServerLogWindow.isVisible = true }
            onServerConfig.invokeLater { SelectServerDialog.isVisible = true }
            onBotDirConfig.invokeLater { BotDirectoryConfigDialog.isVisible = true }
        }

        Client.apply {
            onGameStarted.subscribe { showBattle() }
//            onGameEnded.subscribe { showLogo() }
//            onGameAborted.subscribe { showLogo() }
        }

        onClosing {
            BootstrapProcess.stopRunning()
            close()
        }
    }

    private fun selectBots() {
        var disposable: Closeable? = null
        disposable = Client.onConnected.subscribe {
            NewBattleDialog.isVisible = true
            // Make sure to dispose. Otherwise the dialog will be shown when testing if the server is running
            disposable?.close()
        }
        PrepareServerCommand().execute()
    }

    private fun showLogo() {
        contentPane.apply {
            remove(BattlePanel)
            add(LogoPanel)
        }
        validate()
        repaint()
    }

    private fun showBattle() {
        contentPane.remove(LogoPanel)
        contentPane.add(BattlePanel)

        validate()
        repaint()
    }

    override fun close() {
        Client.close()
        BootstrapProcess.stopRunning()
        ServerProcess.stop()
    }
}

@UnstableDefault
@ImplicitReflectionSerializer
private fun main() {
    Runtime.getRuntime().addShutdownHook(Thread {
        MainWindow.close()
    })

    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

    EventQueue.invokeLater {
        MainWindow.isVisible = true
    }
}