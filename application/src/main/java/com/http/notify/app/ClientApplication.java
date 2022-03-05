package com.http.notify.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "notify", version = "0.0.1",
    sortOptions = false, helpCommand = true, mixinStandardHelpOptions = true,
    header = {
"\n" +
    " _      ____  _____  _  _____ _  _____ ____ \n" +
    "/ \\  /|/  _ \\/__ __\\/ \\/    // \\/  __//  __\\\n" +
    "| |\\ ||| / \\|  / \\  | ||  __\\| ||  \\  |  \\/|\n" +
    "| | \\||| \\_/|  | |  | || |   | ||  /_ |    /\n" +
    "\\_/  \\|\\____/  \\_/  \\_/\\_/   \\_/\\____\\\\_/\\_\\\n" +
    "                                            \n"
})
public class ClientApplication implements  Runnable {

    private static final Logger log = LoggerFactory.getLogger(ClientApplication.class);

    //client inputted url
    @CommandLine.Option(names = {"-u", "--url"}, paramLabel = "<URL>", required = true, description = "The notifier configured url.")
    String url;

    //client message
    @CommandLine.Option(names = {"-m", "--message"}, paramLabel = "<MESSAGE>", required = true, description = "The message to be sent.")
    String message;

    //interval for which message event will be delivered
    @CommandLine.Option(names = {"-i", "--interval"}, paramLabel = "<interval>",
        description = "Interval of sending the message to the notifier client.")
    int interval;

    @Override
    public void run() {
        new Runner(url, message, interval).run();
    }

    //Entry point for cli runner
    public static void main(String[] args) {
        ClientApplication application = new ClientApplication();
        int exitCode = new CommandLine(application).execute(args);
        log.info("-------------{}-------------", exitCode);
    }

}
