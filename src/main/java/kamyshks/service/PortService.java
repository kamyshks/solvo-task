package kamyshks.service;

import kamyshks.dto.LocationDto;
import kamyshks.exceptions.TransactionException;
import kamyshks.repository.LoadRepository;
import kamyshks.repository.LocationRepository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class PortService {

    private final LoadRepository loadRepository;
    private final LocationRepository locationRepository;

    public PortService(){
        loadRepository = new LoadRepository();
        locationRepository = new LocationRepository();
    }

    public void addLoads(final int countLoads, final String locationName){
        try {
            final int id = locationRepository.getIdLocationByName(locationName);
            if (id == -1) {
                throw new TransactionException("The location with current id not found");
            }

            for (int i=0; i<countLoads; i++){
                loadRepository.insertLoad(id);
            }
        } catch (TransactionException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void getAndPrint_CountLoadsInLocations(final String locationNames){
        try {
            Map<String, Integer> countLoads = loadRepository.getCountLoads(locationNames);
            System.out.println("location name : count loads");
            Arrays.stream(locationNames.split(",")).forEach(l -> {
                if(!countLoads.containsKey(l)) countLoads.put(l,0);
            });

            countLoads.forEach((key, value) -> System.out.println(key + " : " + value));
        }catch (TransactionException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void exportToXML(final String fileName){
        List<LocationDto> all = loadRepository.getAllLoads();
        try (FileOutputStream output = new FileOutputStream(fileName)) {
            writeXml(createDoc(all), output);
        } catch (IOException | ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public Document createDoc(final List<LocationDto> all) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("dbinfo");
        doc.appendChild(rootElement);

        all.forEach(l -> {
            Element location = doc.createElement("location");
            location.setAttribute("id", l.getId().toString());
            location.setAttribute("name", l.getName());
            rootElement.appendChild(location);

            l.getLoads().forEach(load -> {
                Element loadElement = doc.createElement("load");
                loadElement.setAttribute("id", load.getId().toString());
                loadElement.setAttribute("name", load.getName());
                location.appendChild(loadElement);
            });

        });
        return doc;
    }

    private void writeXml(Document doc, OutputStream output) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(doc), new StreamResult(output));
    }
}
