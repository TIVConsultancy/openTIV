/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tivconsultancy.opentiv.helpfunctions.io;

/**
 *
 * @author Cityk
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.tivconsultancy.opentiv.helpfunctions.strings.StringWorker;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Thomas
 */
public class Crawler {

    public static List<String> crawlPathWildCard(String sPath, int iCrawlDeph, List<String> lsToFinde) {
        sPath = sPath + System.getProperty("file.separator");
        List<String> lsPath = new ArrayList<String>();
        String[] entries = new File(sPath).list();
        for (String s : entries) {
            boolean bMatch = false;
            for (String sF : lsToFinde) {
                if (s.contains(sF)) {
                    bMatch = true;
                    break;
                }
            }
            if ((new File(sPath + s).isDirectory()) && iCrawlDeph > 0) {

                lsPath.addAll(crawlPathWildCard(sPath + s, iCrawlDeph - 1, lsToFinde));

            } else {
                if (bMatch) {
                    lsPath.add(sPath + s);
                }
            }
        }
        System.out.println(Arrays.toString(entries));
        return lsPath;
    }

    public static List<String> crawlNameWildCard(String sPath, int iCrawlDeph, List<String> lsToFinde) {
        sPath = sPath + System.getProperty("file.separator");
        List<String> lsPath = new ArrayList<String>();
        String[] entries = new File(sPath).list();
        for (String s : entries) {
            boolean bMatch = false;
            for (String sF : lsToFinde) {
                if (s.contains(sF)) {
                    bMatch = true;
                    break;
                }
            }
            if ((new File(sPath + s).isDirectory()) && iCrawlDeph > 0) {

                lsPath.addAll(crawlNameWildCard(sPath + s, iCrawlDeph - 1, lsToFinde));

            } else {
                if (bMatch) {
                    lsPath.add(s);
                }
            }
        }
        System.out.println(Arrays.toString(entries));
        return lsPath;
    }

    public static List<String> crawlPath(String sPath, int iCrawlDeph, List<String> lsToFinde) {
        sPath = sPath + System.getProperty("file.separator");
        List<String> lsPath = new ArrayList<String>();
        String[] entries = new File(sPath).list();
        for (String s : entries) {
            boolean bMatch = false;
            for (String sF : lsToFinde) {
                if (sF.contains("*")) {
                    if (s.contains(sF.replace("*", ""))) {
                        bMatch = true;
                        break;
                    }
                } else {
                    if (s.equalsIgnoreCase(sF)) {
                        bMatch = true;
                        break;
                    }
                }

            }
            if ((new File(sPath + s).isDirectory()) && iCrawlDeph > 0) {

                lsPath.addAll(crawlPath(sPath + s, iCrawlDeph - 1, lsToFinde));

            } else {
                if (bMatch) {
                    lsPath.add(sPath + s);
                }
            }
        }
//        PrintArray.printToSystemLinela(lsPath);
        return lsPath;
    }

    public static List<String> crawlPath(String sPath, int iCrawlDeph, List<String> lsToFinde, List<String> lsIncludeFilter) {
        List<String> lsPathsNotFiltered = crawlPath(sPath, iCrawlDeph, lsToFinde);
        List<String> lsPaths = new ArrayList<String>();
        for (String sFoundPath : lsPathsNotFiltered) {
            if (StringWorker.contains(sFoundPath, lsIncludeFilter)) {
                lsPaths.add(sFoundPath);
            }
        }

        return lsPaths;
    }

    public static List<String> crawlPathExcludeFilter(String sPath, int iCrawlDeph, List<String> lsToFinde, List<String> lsExcludeFilter) {
        List<String> lsPathsNotFiltered = crawlPath(sPath, iCrawlDeph, lsToFinde);
        List<String> lsPaths = new ArrayList<String>();
        for (String sFoundPath : lsPathsNotFiltered) {
            if (lsExcludeFilter.isEmpty() || !StringWorker.contains(sFoundPath, lsExcludeFilter)) {
                lsPaths.add(sFoundPath);
            }
        }

        return lsPaths;
    }

    public static List<String> crawlFolder(String sPath, int iCrawlDeph, List<String> lsToFinde, boolean bexact) {
        sPath = sPath + System.getProperty("file.separator");
        List<String> lsPath = new ArrayList<String>();
        String[] entries = new File(sPath).list();
        if (entries == null) {
            return new ArrayList<String>();
        }
        for (String s : entries) {
            boolean bMatch = false;
            for (String sF : lsToFinde) {
                if (bexact) {
                    if (s.equalsIgnoreCase(sF)) {
                        bMatch = true;
                        break;
                    }
                } else {
                    if (s.contains(sF)) {
                        bMatch = true;
                        break;
                    }
                }
            }
            if (!bMatch && iCrawlDeph > 0) {

                lsPath.addAll(crawlFolder(sPath + s, iCrawlDeph - 1, lsToFinde, bexact));

            } else {
                if (bMatch) {
                    lsPath.add(sPath + s);
                }
            }
        }
        System.out.println(Arrays.toString(entries));
        return lsPath;
    }

    public static List<String> crawlFolder(String sPath, int iCrawlDeph, String sToFinde, boolean bexact) {

        List<String> ls = new ArrayList<String>();
        ls.add(sToFinde);
        return crawlFolder(sPath, iCrawlDeph, ls, bexact);

    }

    public static List<String> getAllFolderNames(String sPWD) {

        String[] entries = new File(sPWD).list();
        List<String> lsFolders = new ArrayList<String>();
        for (String s : entries) {
            if ((new File(sPWD + s).isDirectory())) {
                lsFolders.add(s);
            }
        }

        return lsFolders;
    }

    public static List<String> getAllFiles(String sPWD) {

        String[] entries = new File(sPWD).list();
        List<String> lsFiles = new ArrayList<String>();
        for (String s : entries) {
            if (!(new File(sPWD + s).isDirectory())) {
                lsFiles.add(s);
            }
        }

        return lsFiles;
    }

    public static List<String> getAllFiles(String sPWD, String sExtension) {

        String[] entries = new File(sPWD).list();
        List<String> lsFiles = new ArrayList<String>();
        if (entries != null) {
            for (String s : entries) {
                File of = new File(sPWD + s);
                try {
                    if (!of.isDirectory() && of.getName().substring(of.getName().lastIndexOf(".")).equals(sExtension)) {
                        lsFiles.add(s);
                    }
                } catch (Exception e) {
                }

            }
        }

        return lsFiles;
    }


}

