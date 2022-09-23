package kamyshks;

import kamyshks.service.ConnectService;
import kamyshks.service.PortService;
import org.apache.commons.cli.*;

public class Main {

    public static void main(String[] args) {
        ConnectService.initialDB();

        final Options options = new Options();

        final Option optServerUrl = new Option("s", "save", false, "save loads");
        optServerUrl.setRequired(false);
        options.addOption(optServerUrl);

        final Option optFilePath = new Option("c", "countLoads", true, "input count loads");
        optFilePath.setRequired(false);
        options.addOption(optFilePath);

        final Option optRCount = new Option("l", "locationName", true, "input location name");
        optRCount.setRequired(false);
        optRCount.setType(Integer.class);
        options.addOption(optRCount);

        final Option optWCount = new Option("g", "locationNames", true, "Get count loads. Input locations names (like: 104,55,134)");
        optWCount.setRequired(false);
        optWCount.setType(Integer.class);
        options.addOption(optWCount);

        final Option optIdList = new Option("e", "fileName", true, "Export to xml file. Input file name");
        optIdList.setRequired(false);
        options.addOption(optIdList);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        try {
            final CommandLine cmd = parser.parse(options, args);


            final String countLoads = cmd.getOptionValue("countLoads");
            final String locationName = cmd.getOptionValue("locationName");
            final String locationNames = cmd.getOptionValue("locationNames");
            final String fileName = cmd.getOptionValue("fileName");

            final PortService portService = new PortService();

            if(cmd.hasOption("save") && countLoads != null && locationName != null){
                portService.addLoads(Integer.parseInt(countLoads), locationName);
            }

            if (locationNames != null) {
                portService.getAndPrint_CountLoadsInLocations(locationNames);
            }

            if (fileName != null) {
                portService.exportToXML(fileName);
            }
        } catch (ParseException| NullPointerException | NumberFormatException e) {
            System.out.println("Incorrect command parameters");
            formatter.printHelp("Balance client", options);
            System.exit(1);
        }
    }
}
