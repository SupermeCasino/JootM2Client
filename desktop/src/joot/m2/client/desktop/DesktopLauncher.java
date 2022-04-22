package joot.m2.client.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import joot.m2.client.App;
import joot.m2.client.util.AssetUtil;
import joot.m2.client.util.NetworkUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class DesktopLauncher {
    public static void main(String[] args) {

        Options options = new Options()
                .addOption(Option.builder("p").longOpt("path").desc("JootM2Client Folder Path").hasArg().build())
                .addOption(Option.builder("srv").longOpt("server").desc("JootM2Client Server IP").hasArg().build())
                .addOption(Option.builder("log").longOpt("log-level").desc("log verbosity for debugging purposes").hasArg().build());

        CommandLine cmd = null;

        try {
            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            // TODO write error log in disk
            System.err.println(e.getMessage());
        } finally {
            if (cmd == null || cmd.getOptions().length == 0) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("JootM2Client", options);
                System.exit(0);
            }
        }
        String path = "/Users/linxing/m2/ShengquGames/Legend of mir";
        if (cmd.hasOption("path")) {
            path = cmd.getOptionValue("path");
        }
        AssetUtil.init(path);

        String server = "ws://127.0.0.1:55842/m2";
        if (cmd.hasOption("server")) {
            server = cmd.getOptionValue("server");
        }
        NetworkUtil.init(server);
        var config = new LwjglApplicationConfiguration();
        config.resizable = true;
        config.width = 800;
        config.height = 600;
        config.title = "将唐传奇";
        config.addIcon("mir.jpg", FileType.Internal);
        new LwjglApplication(new App(), config);
    }
}
