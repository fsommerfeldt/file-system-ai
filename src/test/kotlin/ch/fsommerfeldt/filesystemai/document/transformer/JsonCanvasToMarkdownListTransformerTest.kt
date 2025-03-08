package ch.fsommerfeldt.filesystemai.document.transformer

import dev.langchain4j.data.document.Document
import dev.langchain4j.data.document.Metadata
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class JsonCanvasToMarkdownListTransformerTest {

    private val jsonCanvas = """
        {
            "nodes":[
                {"id":"ebecc222e3177de8","type":"text","text":"Back to Screen","x":-560,"y":-480,"width":250,"height":60,"color":"3"},
                {"id":"1f22fc3ebf34f9e5","type":"text","text":"Vergleich mit anderem OS","x":-560,"y":-400,"width":250,"height":60,"color":"3"},
                {"id":"ee9b4441816631d7","type":"text","text":"Vergleich mit Web-Shop","x":-560,"y":-320,"width":250,"height":60,"color":"3"},
                {"id":"69a42f0c5bc86a70","type":"text","text":"Light- und Darkmode","x":-440,"y":-600,"width":250,"height":60,"color":"3"},
                {"id":"f94e3f8802917145","type":"text","text":"Font-Size, Screen-Size","x":-160,"y":-600,"width":250,"height":60,"color":"3"},
                {"id":"12ff9f20d15ea6eb","type":"text","text":"Frontend","x":-160,"y":-400,"width":260,"height":60,"color":"3"},
                {"id":"d20763c7a8673809","type":"text","text":"Einhandmodus","x":120,"y":-600,"width":250,"height":60,"color":"3"},
                {"id":"36f71b9a8ee31551","type":"text","text":"Sprachen","x":240,"y":-480,"width":250,"height":60,"color":"3"},
                {"id":"00887fa56b330bc2","type":"text","text":"Langsame Verbindung","x":240,"y":-400,"width":250,"height":60,"color":"3"},
                {"id":"b490d1f31c61e7a5","type":"text","text":"Alte App mit neuem Backend","x":240,"y":-320,"width":250,"height":60,"color":"3"},
                {"id":"77f616ff9feab59e","type":"text","text":"Allgemein","x":-160,"y":-123,"width":250,"height":60},
                {"id":"234da9a3bb57533d","type":"text","text":"Navigation","x":180,"y":-160,"width":250,"height":60,"color":"4"},
                {"id":"d86924338f8617d1","type":"text","text":"Wird sich der Screen gemerkt beim Tab-Wechsel?","x":540,"y":-160,"width":300,"height":58,"color":"4"},
                {"id":"34cf6181347261a0","type":"text","text":"Im Flow zurück gehen","x":540,"y":-80,"width":250,"height":60,"color":"4"},
                {"id":"b13402f8a2f694d6","type":"text","text":"Modal schliessen","x":540,"y":-240,"width":250,"height":60,"color":"4"},
                {"id":"dd4426064391d40e","type":"text","text":"Tab-Bar","x":540,"y":0,"width":250,"height":60,"color":"4"},
                {"id":"2728ea16cc77bf3c","type":"text","text":"Geräte","x":-440,"y":60,"width":250,"height":60},
                {"id":"0af74ec2d2afd79b","type":"text","text":"Tablet","x":-580,"y":180,"width":250,"height":60},
                {"id":"2dba5071d4131526","type":"text","text":"Smartphone","x":-310,"y":180,"width":250,"height":60},
                {"id":"e6e275fe9cf950ea","type":"text","text":"iOS","x":-40,"y":180,"width":250,"height":60,"color":"6"},
                {"id":"08f2e73e92843682","type":"text","text":"Android","x":240,"y":180,"width":250,"height":60,"color":"6"},
                {"id":"651f87a56cf1d297","type":"text","text":"OS","x":106,"y":60,"width":250,"height":60,"color":"6"},
                {"id":"1417ce4c8281a745","type":"text","text":"Pull to Refresh","x":-560,"y":-240,"width":250,"height":60,"color":"3"}
            ],
            "edges":[
                {"id":"663ee3678e803124","fromNode":"2728ea16cc77bf3c","fromSide":"bottom","toNode":"2dba5071d4131526","toSide":"top"},
                {"id":"e638e15af1305b01","fromNode":"2728ea16cc77bf3c","fromSide":"bottom","toNode":"0af74ec2d2afd79b","toSide":"top"},
                {"id":"8deac7d5c8e1d580","fromNode":"651f87a56cf1d297","fromSide":"bottom","toNode":"08f2e73e92843682","toSide":"top"},
                {"id":"87f1317dd67bc109","fromNode":"651f87a56cf1d297","fromSide":"bottom","toNode":"e6e275fe9cf950ea","toSide":"top"},
                {"id":"a1bd2e9159b7f8cd","fromNode":"77f616ff9feab59e","fromSide":"bottom","toNode":"651f87a56cf1d297","toSide":"left"},
                {"id":"51529f7a26fbdbda","fromNode":"77f616ff9feab59e","fromSide":"bottom","toNode":"2728ea16cc77bf3c","toSide":"right"},
                {"id":"72bca611292b514c","fromNode":"234da9a3bb57533d","fromSide":"right","toNode":"d86924338f8617d1","toSide":"left"},
                {"id":"1209562f9e3eadf2","fromNode":"234da9a3bb57533d","fromSide":"right","toNode":"34cf6181347261a0","toSide":"left"},
                {"id":"6234497efd6d737d","fromNode":"77f616ff9feab59e","fromSide":"top","toNode":"12ff9f20d15ea6eb","toSide":"bottom"},
                {"id":"770fe0d531b6b255","fromNode":"234da9a3bb57533d","fromSide":"right","toNode":"b13402f8a2f694d6","toSide":"left"},
                {"id":"ac82adf370cd240b","fromNode":"12ff9f20d15ea6eb","fromSide":"top","toNode":"f94e3f8802917145","toSide":"bottom"},
                {"id":"7caa194768944ece","fromNode":"12ff9f20d15ea6eb","fromSide":"top","toNode":"d20763c7a8673809","toSide":"bottom"},
                {"id":"7628424bdce3e832","fromNode":"12ff9f20d15ea6eb","fromSide":"top","toNode":"69a42f0c5bc86a70","toSide":"bottom"},
                {"id":"dfa22eedf0556d9b","fromNode":"12ff9f20d15ea6eb","fromSide":"left","toNode":"ebecc222e3177de8","toSide":"right"},
                {"id":"e39bd8bfc3ac6749","fromNode":"12ff9f20d15ea6eb","fromSide":"left","toNode":"1f22fc3ebf34f9e5","toSide":"right"},
                {"id":"739db101a77efdd9","fromNode":"12ff9f20d15ea6eb","fromSide":"left","toNode":"ee9b4441816631d7","toSide":"right"},
                {"id":"7cd44ad23cb93b69","fromNode":"12ff9f20d15ea6eb","fromSide":"right","toNode":"36f71b9a8ee31551","toSide":"left"},
                {"id":"0d4da349efa4fa39","fromNode":"234da9a3bb57533d","fromSide":"right","toNode":"dd4426064391d40e","toSide":"left"},
                {"id":"27b8fdc8bc0beaae","fromNode":"12ff9f20d15ea6eb","fromSide":"right","toNode":"00887fa56b330bc2","toSide":"left"},
                {"id":"45dd5e5b699b20a0","fromNode":"12ff9f20d15ea6eb","fromSide":"right","toNode":"b490d1f31c61e7a5","toSide":"left"},
                {"id":"5913b8e0e8ebd289","fromNode":"12ff9f20d15ea6eb","fromSide":"left","toNode":"1417ce4c8281a745","toSide":"right"},
                {"id":"9adad76b953bc1eb","fromNode":"12ff9f20d15ea6eb","fromSide":"right","toNode":"234da9a3bb57533d","toSide":"left"}
            ]
        }
    """

    @Test
    fun shouldTransformToMarkdownList() {
        val metadata = Metadata(mapOf(Pair("absolute_directory_path", "/path/to/directory"), Pair("file_name", "file.canvas")))
        val jsonCanvasDoc = Document.from(jsonCanvas, metadata)

        val transformer = JsonCanvasToMarkdownListTransformer()
        val markdownList = transformer.transform(jsonCanvasDoc)

        val expectedMarkdown = """
- Allgemein
	- OS
		- Android
		- iOS
	- Geräte
		- Smartphone
		- Tablet
	- Frontend
		- Font-Size, Screen-Size
		- Einhandmodus
		- Light- und Darkmode
		- Back to Screen
		- Vergleich mit anderem OS
		- Vergleich mit Web-Shop
		- Sprachen
		- Langsame Verbindung
		- Alte App mit neuem Backend
		- Pull to Refresh
		- Navigation
			- Wird sich der Screen gemerkt beim Tab-Wechsel?
			- Im Flow zurück gehen
			- Modal schliessen
			- Tab-Bar
        """.trim()

        assertEquals(expectedMarkdown, markdownList.text().trim())
    }

}