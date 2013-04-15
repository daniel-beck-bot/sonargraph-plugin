package com.hello2morrow.sonargraph.jenkinsplugin.persistence;

import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hello2morrow.sonargraph.jenkinsplugin.foundation.RecorderLogger;

import de.schlichtherle.truezip.file.TFile;

/**
 * Class that handles copies of each generated architect report to calculate trends or
 * generate graphics.
 * @author esteban
 *
 */
public class ReportHistoryFileManager
{
    public static final String SONARGRAPH_JENKINS_REPORT_FILE_NAME_PREFIX = "sonargraph-jenkins-report-build-";

    /** Path to the folder containing sonargraph report files generated for every build */
    private TFile m_sonargraphReportHistoryDir;

    public ReportHistoryFileManager(TFile projectRootDir, String reportHistoryDirName, PrintStream logger)
    {
        assert projectRootDir != null : "The path to the folder where architect reports are stored must not be null";
        assert logger != null : "Parameter 'logger' of method 'ReportHistoryFileManager' must not be null";

        m_sonargraphReportHistoryDir = new TFile(projectRootDir, reportHistoryDirName);
        if (!m_sonargraphReportHistoryDir.exists())
        {
            try
            {
                m_sonargraphReportHistoryDir.mkdir(true);
            }
            catch (IOException ex)
            {
                RecorderLogger.logToConsoleOutput(logger, Level.SEVERE,
                        "Failed to create directory '" + m_sonargraphReportHistoryDir.getNormalizedAbsolutePath() + "'");
            }
        }
    }

    public TFile getReportHistoryDirectory()
    {
        return m_sonargraphReportHistoryDir;
    }

    /**
     * Stores a generated architect report in the location defined for this purpose. 
     * @param architectReport the architect report file.
     */
    public void storeGeneratedReport(TFile architectReport, Integer buildNumber, PrintStream logger) throws IOException
    {
        assert architectReport != null : "Parameter 'architectReport' of method 'storeGeneratedReport' must not be null";
        assert architectReport.exists() : "Parameter 'architectReport' must be an existing file. '" + architectReport.getNormalizedAbsolutePath()
                + "' does not exist.";

        if ((architectReport == null) || (buildNumber == null))
        {
            return;
        }

        if (!m_sonargraphReportHistoryDir.exists())
        {
            String msg = "Unable to create directory " + m_sonargraphReportHistoryDir.getAbsolutePath();
            RecorderLogger.logToConsoleOutput(logger, Level.SEVERE, msg);
            throw new IOException(msg);
        }

        Pattern extensionPattern = Pattern.compile("\\.[a-zA-Z0-9]*$");
        Matcher extensionMatcher = extensionPattern.matcher(architectReport.getAbsolutePath());
        String extension = extensionMatcher.find() ? extensionMatcher.group() : "";
        TFile to = new TFile(m_sonargraphReportHistoryDir, SONARGRAPH_JENKINS_REPORT_FILE_NAME_PREFIX + buildNumber + extension);
        TFile.cp(architectReport, to);
    }

    public void storeGeneratedReportDirectory(TFile reportDirectory, Integer buildNumber, PrintStream logger) throws IOException
    {
        assert reportDirectory != null : "Parameter 'reportDirectory' of method 'soterdGeneratedReportDirectory' must not be null";
        assert reportDirectory.exists() : "Parameter 'reportDirectory' must be an existing folder. '" + reportDirectory.getNormalizedAbsolutePath()
                + "' does not exist.";

        if (!m_sonargraphReportHistoryDir.exists())
        {
            String msg = "Unable to create directory " + m_sonargraphReportHistoryDir.getAbsolutePath();
            RecorderLogger.logToConsoleOutput(logger, Level.SEVERE, msg);
            throw new IOException(msg);
        }
        reportDirectory.cp_r(new TFile(m_sonargraphReportHistoryDir, "sonargraph-report-build-" + buildNumber));
    }
}
