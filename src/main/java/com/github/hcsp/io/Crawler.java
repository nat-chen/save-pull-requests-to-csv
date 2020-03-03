package com.github.hcsp.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Crawler {
    // 给定一个仓库名，例如"golang/go"，或者"gradle/gradle"，读取前n个Pull request并保存至csvFile指定的文件中，格式如下：
    // number,author,title
    // 12345,blindpirate,这是一个标题
    // 12345,FrankFang,这是第二个标题
    public static void savePullRequestsToCSV(String repo, int n, File csvFile) throws IOException {
        GitHub github = GitHub.connectAnonymously();

        GHRepository repository = github.getRepository(repo);

        List<GHPullRequest> pullRequests = repository.getPullRequests(GHIssueState.OPEN);

        createCSVFile(csvFile, pullRequests.subList(0, n));

    }

    public static void main(String[] args) throws IOException {
        savePullRequestsToCSV("golang/go", 10, new File("pull-request.csv"));
    }

    public static void createCSVFile(File file, List<GHPullRequest> list) throws IOException {
        FileWriter out = new FileWriter(file);
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader("number", "author", "title"))) {
            list.forEach(gHPullRequest -> {
                try {
                    printer.printRecord(gHPullRequest.getNumber(), gHPullRequest.getUser().getLogin(), gHPullRequest.getTitle());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
