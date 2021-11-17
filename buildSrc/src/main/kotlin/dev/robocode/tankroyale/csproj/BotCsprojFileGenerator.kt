package dev.robocode.tankroyale.csproj

import dev.robocode.tankroyale.xml.attribute
import dev.robocode.tankroyale.xml.document
import dev.robocode.tankroyale.xml.element
import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import javax.xml.stream.XMLOutputFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

fun generateBotCsprojTempFile(botName: String, botApiVersion: String): Path {
    val path = Files.createTempFile(botName, ".csproj")
    generateBotCsprojFile(path, botName, botApiVersion)
    return path
}

fun generateBotCsprojFile(outputFilePath: Path, botName: String, botApiVersion: String) {
    writePrettyXml(outputFilePath, botName, botApiVersion)
}

private fun writePrettyXml(outputFilePath: Path, botName: String, botApiVersion: String) {
    val byteArrayOut = ByteArrayOutputStream()

    writeXml(byteArrayOut, botName, botApiVersion)

    val xml = byteArrayOut.toString(StandardCharsets.UTF_8)
    val prettyXml = formatXML(xml)

    Files.writeString(outputFilePath, prettyXml, StandardCharsets.UTF_8)
}

private fun writeXml(out: OutputStream, botName: String, botApiVersion: String) {

    val writer = XMLOutputFactory.newInstance().createXMLStreamWriter(
        OutputStreamWriter(out, StandardCharsets.UTF_8)
    )
    writer.document {
        element("project") {
            attribute("Sdk", "Microsoft.NET.Sdk")
            element("PropertyGroup") {
                element("RootNamespace", botName)
                element("OutputType", "Exe")
                element("TargetFramework", "net5.0")
                element("LangVersion", "8.0")
            }
            element("ItemGroup") {
                element("PackageReference") {
                    attribute("Include", "Robocode.TankRoyale.BotApi")
                    attribute("Version", botApiVersion)
                }
            }
        }
    }
    writer.flush()
    writer.close()
}

private fun formatXML(xml: String): String {

    val transformerFactory = TransformerFactory.newInstance()
    val transformer = transformerFactory.newTransformer()

    val source = StreamSource(StringReader(xml))
    val output = StringWriter()

    with(transformer) {
        setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
        setOutputProperty(OutputKeys.INDENT, "yes")
        setOutputProperty(OutputKeys.STANDALONE, "yes")

        transform(source, StreamResult(output))
    }

    return output.toString()
}

fun main() {
//    generateBotCsprojFile(Paths.get("C:\\Windows\\Temp\\test.xml"), "Corners", "0.9.10")

    println(generateBotCsprojTempFile("Corners", "0.9.10"))
}