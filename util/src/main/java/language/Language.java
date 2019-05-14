package language;

import util.Logger;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * 
 * 
 * Esta clase permite obtener el texto de una etiqueta xml pasandole el nombre
 * de la etiqueta. La clase esta diseñada con un patron de diseño singleton.
 *
 * Los archivos xml de idioma deben encontrarse donde indique la variable
 * xmlpath, por defecto en //languages/strings/ Cada xml debe ser de la forma
 * (ejemplo): spanish.xml
 *
 * <?xml version="1.0" encoding="iso-8859-1"?>
 * <language>
 * <file>Archivo</file>
 * <edit>Edición</edit>
 * <error1>Error al introducir los datos</error1>
 * </language>
 *
 * Todos deben contener las mismas etiquetas para el correcto funcionamiento del
 * programa, aunque no es necesario que tengan el mismo orden.
 *
 * @author Daniel Plaza
 */
public class Language extends Logger{
    
    private Document doc;
    private final File langFile;

    public Language(String languageFile,String configLogger) {
        super(new File(configLogger));
        langFile= new File(languageFile);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            if (!new File(langFile.toString()).exists()) {
                throw new IllegalArgumentException(langFile.getName()+" not found");
            }
            doc = dBuilder.parse(langFile);
            doc.getDocumentElement().normalize();
            log("\""+langFile.getName()+"\" loaded");
        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace(System.err);
        }
    }

    public Language(String languageFile){
        this(languageFile, null);
    }

    /**
     *
     * Método que devuelve el texto de la etiqueta.
     *
     * Si hay más de una etiqueta con el mismo nombre devolverá la primera
     * escribiendo en el log un mensaje advirtiendo del conflicto.
     *
     * Si no encuentra la etiqueta devolvera "#<nombreEtiqueta>" y mostrará un
     * mensaje por el log avisando del conflicto.
     *
     * @param markup nombre de la etiqueta
     * @return texto de la etiqueta
     */
    public String getText(String markup) {
        NodeList nodeList = doc.getElementsByTagName(markup);
        try {
            if (nodeList.getLength() > 1) {
                log("Found " + nodeList.getLength() + " <" + markup + "> items in " + langFile.getName());
            }
            return nodeList.item(0).getTextContent();
        } catch (NullPointerException e) {
            log("Cannot find any <" + markup + "> item in " + langFile.getName());
            return "#Not found <" + markup + ">";
        }
    }

}
