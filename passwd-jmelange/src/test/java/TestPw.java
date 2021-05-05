import com.github.terefang.jmelange.passwd.PasswdUtil;
import com.github.terefang.jmelange.passwd.crypt.*;

public class TestPw {
    public static void main(String[] args)
    {
        System.out.println(BsdNtCrypt.crypt("s3cr3t"));
        System.out.println(BsdNtCrypt.crypt("password"));

        System.out.println(DesCrypt.crypt("s3cr3t"));
        System.out.println(DesCrypt.crypt("password"));

        System.out.println(Md5Crypt.md5Crypt("s3cr3t".getBytes()));
        System.out.println(Md5Crypt.md5Crypt("password".getBytes()));
        System.out.println(Md5Crypt.apr1Crypt("s3cr3t".getBytes()));
        System.out.println(Md5Crypt.apr1Crypt("password".getBytes()));

        System.out.println(Mysql4Crypt.crypt("s3cr3t"));
        System.out.println(Mysql4Crypt.crypt("password"));

        System.out.println(BCrypt.generate("s3cr3t"));
        System.out.println(BCrypt.generate("password"));

        System.out.println(SCrypt.scrypt("s3cr3t"));
        System.out.println(SCrypt.scrypt("password"));

        System.out.println(Sha1Crypt.crypt("s3cr3t"));
        System.out.println(Sha1Crypt.crypt("password"));
        System.out.println(Sha1Crypt.simpleCrypt("s3cr3t"));
        System.out.println(Sha1Crypt.simpleCrypt("password"));

        System.out.println(Sha2Crypt.sha256Crypt("s3cr3t".getBytes()));
        System.out.println(Sha2Crypt.sha512Crypt("password".getBytes()));

        System.out.println(PCrypt.crypt_pbkdf2_sha1("s3cr3t"));
        System.out.println(PCrypt.crypt_pbkdf2_sha1("password"));

        System.out.println(PCrypt.atlassian_pbkdf2_sha1("s3cr3t"));
        System.out.println(PCrypt.atlassian_pbkdf2_sha1("password"));

        System.out.println(PCrypt.radiator_pbkdf2("s3cr3t"));
        System.out.println(PCrypt.radiator_pbkdf2("password"));

        System.out.println(Rfs2307Crypt.checkPw("password", "{SSHA}pKqkNr1tq3wtQqk+UcPyA3HnA2NsU5NJ"));
        System.out.println(Rfs2307Crypt.checkPw("password","{SMD5}jNoSMNY0cybfuBWiaGlFw3Mfi/U="));

        String _test = PasswdUtil.hashPassword("-nil-", "password");
        System.out.println(_test);
        System.out.println(PasswdUtil.matchPassword("password", _test));
    }
}
