package com.github.terefang.jmelange.commons.util;

import lombok.SneakyThrows;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.io.InputStreamFacade;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class FileUtil
{

    public static boolean contentEquals(File file1, File file2) throws IOException {
        return FileUtils.contentEquals(file1, file2);
    }

    public static File toFile(URL url) {
        return FileUtils.toFile(url);
    }

    public static URL[] toURLs(File[] files) throws IOException {
        return FileUtils.toURLs(files);
    }

    public static String removeExtension(String filename) {
        return FileUtils.removeExtension(filename);
    }

    public static String getExtension(String filename) {
        return FileUtils.getExtension(filename);
    }

    public static String removePath(String filepath) {
        return FileUtils.removePath(filepath);
    }

    public static String removePath(String filepath, char fileSeparatorChar) {
        return FileUtils.removePath(filepath, fileSeparatorChar);
    }

    public static String getPath(String filepath) {
        return FileUtils.getPath(filepath);
    }

    public static String getPath(String filepath, char fileSeparatorChar) {
        return FileUtils.getPath(filepath, fileSeparatorChar);
    }

    public static void copyFileToDirectory(String source, String destinationDirectory) throws IOException {
        FileUtils.copyFileToDirectory(source, destinationDirectory);
    }

    public static void copyFileToDirectoryIfModified(String source, String destinationDirectory) throws IOException {
        FileUtils.copyFileToDirectoryIfModified(source, destinationDirectory);
    }

    public static void copyFileToDirectory(File source, File destinationDirectory) throws IOException {
        FileUtils.copyFileToDirectory(source, destinationDirectory);
    }

    public static void copyFileToDirectoryIfModified(File source, File destinationDirectory) throws IOException {
        FileUtils.copyFileToDirectoryIfModified(source, destinationDirectory);
    }

    public static void copyFile(File source, File destination) throws IOException {
        FileUtils.copyFile(source, destination);
    }

    public static boolean copyFileIfModified(File source, File destination) throws IOException {
        return FileUtils.copyFileIfModified(source, destination);
    }

    public static void copyURLToFile(URL source, File destination) throws IOException {
        FileUtils.copyURLToFile(source, destination);
    }

    public static void copyStreamToFile(InputStreamFacade source, File destination) throws IOException {
        FileUtils.copyStreamToFile(source, destination);
    }

    public static String normalize(String path) {
        return FileUtils.normalize(path);
    }

    public static String catPath(String lookupPath, String path) {
        return FileUtils.catPath(lookupPath, path);
    }

    public static File resolveFile(File baseFile, String filename) {
        return FileUtils.resolveFile(baseFile, filename);
    }

    public static void forceDelete(String file) throws IOException {
        FileUtils.forceDelete(file);
    }

    public static void forceDelete(File file) throws IOException {
        FileUtils.forceDelete(file);
    }

    public static void forceDeleteOnExit(File file) throws IOException {
        FileUtils.forceDeleteOnExit(file);
    }

    public static void forceMkdir(File file) throws IOException {
        FileUtils.forceMkdir(file);
    }

    public static void deleteDirectory(String directory) throws IOException {
        FileUtils.deleteDirectory(directory);
    }

    public static void deleteDirectory(File directory) throws IOException {
        FileUtils.deleteDirectory(directory);
    }

    public static void cleanDirectory(String directory) throws IOException {
        FileUtils.cleanDirectory(directory);
    }

    public static void cleanDirectory(File directory) throws IOException {
        FileUtils.cleanDirectory(directory);
    }

    public static long sizeOfDirectory(String directory) {
        return FileUtils.sizeOfDirectory(directory);
    }

    public static long sizeOfDirectory(File directory) {
        return FileUtils.sizeOfDirectory(directory);
    }

    public static List<File> getFiles(File directory, String includes, String excludes) throws IOException {
        return FileUtils.getFiles(directory, includes, excludes);
    }

    public static List<File> getFiles(File directory, String includes, String excludes, boolean includeBasedir) throws IOException {
        return FileUtils.getFiles(directory, includes, excludes, includeBasedir);
    }

    public static List<String> getFileNames(File directory, String includes, String excludes, boolean includeBasedir) throws IOException {
        return FileUtils.getFileNames(directory, includes, excludes, includeBasedir);
    }

    public static List<String> getFileNames(File directory, String includes, String excludes, boolean includeBasedir, boolean isCaseSensitive) throws IOException {
        return FileUtils.getFileNames(directory, includes, excludes, includeBasedir, isCaseSensitive);
    }

    public static List<String> getDirectoryNames(File directory, String includes, String excludes, boolean includeBasedir) throws IOException {
        return FileUtils.getDirectoryNames(directory, includes, excludes, includeBasedir);
    }

    public static List<String> getDirectoryNames(File directory, String includes, String excludes, boolean includeBasedir, boolean isCaseSensitive) throws IOException {
        return FileUtils.getDirectoryNames(directory, includes, excludes, includeBasedir, isCaseSensitive);
    }

    public static List<String> getFileAndDirectoryNames(File directory, String includes, String excludes, boolean includeBasedir, boolean isCaseSensitive, boolean getFiles, boolean getDirectories) throws IOException {
        return FileUtils.getFileAndDirectoryNames(directory, includes, excludes, includeBasedir, isCaseSensitive, getFiles, getDirectories);
    }

    public static void copyDirectory(File sourceDirectory, File destinationDirectory) throws IOException {
        FileUtils.copyDirectory(sourceDirectory, destinationDirectory);
    }

    public static void copyDirectory(File sourceDirectory, File destinationDirectory, String includes, String excludes) throws IOException {
        FileUtils.copyDirectory(sourceDirectory, destinationDirectory, includes, excludes);
    }

    public static void copyDirectoryLayout(File sourceDirectory, File destinationDirectory, String[] includes, String[] excludes) throws IOException {
        FileUtils.copyDirectoryLayout(sourceDirectory, destinationDirectory, includes, excludes);
    }

    public static void copyDirectoryStructure(File sourceDirectory, File destinationDirectory) throws IOException {
        FileUtils.copyDirectoryStructure(sourceDirectory, destinationDirectory);
    }

    public static void copyDirectoryStructureIfModified(File sourceDirectory, File destinationDirectory) throws IOException {
        FileUtils.copyDirectoryStructureIfModified(sourceDirectory, destinationDirectory);
    }

    public static void rename(File from, File to) throws IOException {
        FileUtils.rename(from, to);
    }

    public static void rename(String from, String to) throws IOException {
        FileUtils.rename(new File(from), new File(to));
    }

    public static File createTempFile(String prefix, String suffix, File parentDir) {
        return FileUtils.createTempFile(prefix, suffix, parentDir);
    }

    public static File createTempFile(String prefix, String suffix, String parentDir) {
        return FileUtils.createTempFile(prefix, suffix, new File(parentDir));
    }

    public static void copyFile(File from, File to, String encoding, FileUtils.FilterWrapper[] wrappers) throws IOException {
        FileUtils.copyFile(from, to, encoding, wrappers);
    }

    public static void copyFile(File from, File to, String encoding, FileUtils.FilterWrapper[] wrappers, boolean overwrite) throws IOException {
        FileUtils.copyFile(from, to, encoding, wrappers, overwrite);
    }

    public static List<String> loadFile(File file) throws IOException {
        return FileUtils.loadFile(file);
    }

    public static List<String> loadFile(String file) throws IOException {
        return FileUtils.loadFile(new File(file));
    }

    public static boolean isValidWindowsFileName(File f) {
        return FileUtils.isValidWindowsFileName(f);
    }

    public static String fileRead(String file) throws IOException {
        return FileUtils.fileRead(file);
    }

    public static String fileRead(String file, String encoding) throws IOException {
        return FileUtils.fileRead(file, encoding);
    }

    public static String fileRead(File file) throws IOException {
        return FileUtils.fileRead(file);
    }

    public static String fileRead(File file, String encoding) throws IOException {
        return FileUtils.fileRead(file, encoding);
    }

    public static void fileAppend(String fileName, String data) throws IOException {
        FileUtils.fileAppend(fileName, data);
    }

    public static void fileAppend(String fileName, String encoding, String data) throws IOException {
        FileUtils.fileAppend(fileName, encoding, data);
    }

    public static void fileWrite(String fileName, String data) throws IOException {
        FileUtils.fileWrite(fileName, data);
    }

    public static void fileWrite(String fileName, String encoding, String data) throws IOException {
        FileUtils.fileWrite(fileName, encoding, data);
    }

    public static void fileWrite(File file, String data) throws IOException {
        FileUtils.fileWrite(file, data);
    }

    public static void fileWrite(File file, String encoding, String data) throws IOException {
        FileUtils.fileWrite(file, encoding, data);
    }

    public static String fileAbsPath(File _path)
    {
        return _path.getAbsolutePath();
    }

    public static String fileAbsPath(String _path)
    {
        return new File(_path).getAbsolutePath();
    }

    public static String fileName(File _path)
    {
        return _path.getName();
    }

    public static String fileName(String _path)
    {
        return new File(_path).getName();
    }

    public static String filePath(File _path)
    {
        return _path.getPath();
    }

    public static String filePath(String _path)
    {
        return new File(_path).getPath();
    }

    public static String fileParent(File _path)
    {
        return _path.getParent();
    }

    public static String fileParent(String _path)
    {
        return new File(_path).getParent();
    }

    @SneakyThrows
    public static String fileCanonicalPath(File _path)
    {
        return _path.getCanonicalPath();
    }

    @SneakyThrows
    public static String fileCanonicalPath(String _path)
    {
        return new File(_path).getCanonicalPath();
    }

    @SneakyThrows
    public static boolean mkdir(File _path)
    {
        return _path.mkdirs();
    }

    @SneakyThrows
    public static boolean mkdir(String _path)
    {
        return new File(_path).mkdirs();
    }

    @SneakyThrows
    public static boolean fileExists(String _path)
    {
        return new File(_path).exists();
    }

    @SneakyThrows
    public static boolean fileExists(File _path)
    {
        return _path.exists();
    }

    @SneakyThrows
    public static boolean fileIsFile(String _path)
    {
        return new File(_path).isFile();
    }

    @SneakyThrows
    public static boolean fileIsFile(File _path)
    {
        return _path.isFile();
    }

    @SneakyThrows
    public static boolean fileIsDir(String _path)
    {
        return new File(_path).isDirectory();
    }

    @SneakyThrows
    public static boolean fileIsDir(File _path)
    {
        return _path.isDirectory();
    }

    @SneakyThrows
    public static boolean fileDelete(String _path)
    {
        return new File(_path).delete();
    }

    @SneakyThrows
    public static boolean fileDelete(File _path)
    {
        return _path.delete();
    }

    @SneakyThrows
    public static void fileDeleteOnExit(String _path)
    {
        new File(_path).deleteOnExit();
    }

    @SneakyThrows
    public static void fileDeleteOnExit(File _path)
    {
        _path.deleteOnExit();
    }

    @SneakyThrows
    public static long fileLastModified(String _path)
    {
        return new File(_path).lastModified();
    }

    @SneakyThrows
    public static long fileLastModified(File _path)
    {
        return _path.lastModified();
    }

    @SneakyThrows
    public static long fileSize(String _path)
    {
        return new File(_path).length();
    }

    @SneakyThrows
    public static long fileSize(File _path)
    {
        return _path.length();
    }

    public static String fileExtension(String filename)
    {
        return FileUtils.extension(filename);
    }

    public static String[] getFilesFromExtension(String directory, String[] extensions) {
        return FileUtils.getFilesFromExtension(directory, extensions);
    }

}
