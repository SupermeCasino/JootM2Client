package joot.m2.client.desktop;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.github.czyzby.websocket.MyWebSockets;

import joot.m2.client.App;
import joot.m2.client.image.Images;
import joot.m2.client.map.Maps;
import joot.m2.client.util.FontUtil;
import joot.m2.client.util.NetworkUtil;

public class DesktopLauncher {
    public static void main(String[] args) {

        Options options = new Options()
                .addOption(Option.builder("p").longOpt("path").desc("JootM2Client Folder Path").hasArg().build())
                .addOption(Option.builder("srv").longOpt("server").desc("JootM2Client Server IP").hasArg().build())
                .addOption(Option.builder("w").longOpt("weiduan").desc("WeiDuan Base Url").hasArg().build())
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
                System.out.println("如果您不知道如何正确运行，请参考https://www.cnblogs.com/Johness/p/jootm2-how-to-run.html");
                System.exit(0);
            }
        }
        var path = cmd.getOptionValue("path");
        var wdBaseUrl = cmd.getOptionValue("weiduan");
        Maps.init(path, wdBaseUrl);
        Images.init(path, wdBaseUrl);

        String server = cmd.getOptionValue("server");
		MyWebSockets.initiate();
        NetworkUtil.init(server);

		var config = new Lwjgl3ApplicationConfiguration();
		config.setResizable(false);
		config.setWindowedMode(1024, 768);
		config.setWindowIcon(FileType.Internal, "mir.jpg");
		config.setTitle("将唐传奇");
		config.useVsync(true);
	    config.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate);
		new Lwjgl3Application(new App(), config);
		
		FontUtil.shutdown();
		NetworkUtil.shutdown();
		Images.shutdown();
    }
}
