package com.github.terefang.jmelange.passwd;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.util.HashUtil;
import com.github.terefang.jmelange.commons.util.LdataUtil;
import com.github.terefang.jmelange.commons.util.OsUtil;
import com.github.terefang.jmelange.data.ldata.LdataParser;
import com.github.terefang.jmelange.passwd.crypt.*;
import com.github.terefang.jmelange.random.ArcRand;
import lombok.SneakyThrows;

import java.io.File;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.*;

public class PwTool
{

    static Map<String, List<String>> _BASIC_SETS = new LinkedHashMap<>();
    static {
        _BASIC_SETS.put("default", CommonUtil.toList(
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                "abcdefghijklmnopqrstuvwxyz",
                "0123456789",
                "_!§$%&/=?#*+"));
        _BASIC_SETS.put("complex", CommonUtil.toList(
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                "abcdefghijklmnopqrstuvwxyz",
                "0123456789",
                "_!§$%&/=?#*+" ));
        _BASIC_SETS.put("complex2", CommonUtil.toList(
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                "abcdefghijklmnopqrstuvwxyz",
                "0123456789",
                ",.;:-_!§$%&/=?#*+" ));
        _BASIC_SETS.put("mainframe", CommonUtil.toList("ABCDEFGHIJKLM",
                "02468",
                "NOPQRSTUVWXYZ",
                "13579" ));
        _BASIC_SETS.put("flickr-base58", CommonUtil.toList(
                "ABCDEFGHJKLMNPQRSTUVWXYZ",
                "abcdefghijkmnopqrstuvwxyz",
                "123456789" ));
        _BASIC_SETS.put("cookie-base90", CommonUtil.toList(
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                "abcdefghijklmnopqrstuvwxyz",
                "0123456789",
                "!#$%&'()*+-./:<=>?@[]^_`{|}~" ));
        _BASIC_SETS.put("base85", CommonUtil.toList(
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                "abcdefghijklmnopqrstuvwxyz",
                "0123456789",
                "@[\\]^_`!\"#$%&'()*+,-./:;{<|=}>~?" ));
        _BASIC_SETS.put("base62", CommonUtil.toList(
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                "abcdefghijklmnopqrstuvwxyz",
                "0123456789" ));
        _BASIC_SETS.put("rfc4648-base64", CommonUtil.toList(
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                "abcdefghijklmnopqrstuvwxyz",
                "0123456789",
                "+/" ));
        _BASIC_SETS.put("rfc4648-base32", CommonUtil.toList(
                "ABCDEFGHIJKLM",
                "NOPQRSTUVWXYZ",
                "234567" ));
        _BASIC_SETS.put("rfc4648-base32-safeset", CommonUtil.toList(
                "ACDEFGHJKLM",
                "NPQRTUVWXY",
                "3467" ));
        _BASIC_SETS.put("wordsafe", CommonUtil.toList(
                "RVWXcfghjmpqrvwx",
                "23456789",
                "CFGHJMPQ" ));
        _BASIC_SETS.put("safer", CommonUtil.toList(
                "ACDEFGHJKLMNPQRTUVWXY",
                "abcdefghjkmnpqrtuvwxy",
                "34679",
                "$%&*+" ));
        _BASIC_SETS.put("extended", CommonUtil.toList(
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                "abcdefghijklmnopqrstuvwxyz",
                "0123456789",
                "!#%&()*+,-./:;<=>?@[\\]^_{|}~" ));
        readSetFiles();
    }

    private static void readSetFiles()
    {
        String[] _clist = new String[] {
                OsUtil.getUserConfigDirectory("pwtool"),
                OsUtil.getUserDataDirectory("pwtool"),
                OsUtil.getUserConfigDirectory(),
                OsUtil.getUserDataDirectory(),
                OsUtil.getJarDirectory()
        };

        for(String _cpath : _clist)
        {
            File _cfile = new File(_cpath, "pwtool.pdata");
            if(_cfile.exists()) readSetFile(_cfile);
        }
    }

    private static void readSetFile(String _file)
    {
        readSetFile(new File(_file));
    }

    private static void readSetFile(File _cfile)
    {
        if(_cfile.exists())
        {
            for(Map.Entry<String, Object> _entry : LdataParser.loadFrom(_cfile).entrySet())
            {
                _BASIC_SETS.put(_entry.getKey(), ((List<String>)_entry.getValue()));
            }
        }
        else
        {
            System.err.println("does not exist: "+_cfile.getAbsolutePath());
        }
    }

    public static String passwordToPassword(String _password, List<String> _sets, int _len)
    {
        return generatePassword(hash(_password), _sets, _len);
    }

    public static String passwordToPassword(String _password, int _len)
    {
        return generatePassword(hash(_password), _BASIC_SETS.get("default"), _len);
    }

    public static String passwordToPassword(String _password)
    {
        return generatePassword(hash(_password), _BASIC_SETS.get("default"), 16);
    }

    public static String passwordSaltToHash(String _password, String _salt)
    {
        return PasswdUtil.cryptPassword("",_password, _salt);
    }

    public static String passwordToHash(String _password, String _hash)
    {
        if("bcrypt".equalsIgnoreCase(_hash)
                ||"blowfish".equalsIgnoreCase(_hash)
                ||"bfish".equalsIgnoreCase(_hash)
                ||"bf".equalsIgnoreCase(_hash))
        {
            return BcryptFunction.generate(_password);
        }
        else
        if("ntcrypt".equalsIgnoreCase(_hash)
            ||"nt".equalsIgnoreCase(_hash))
        {
            return BsdNtCrypt.crypt(_password);
        }
        else
        if("md5crypt".equalsIgnoreCase(_hash)
                ||"md5".equalsIgnoreCase(_hash))
        {
            return Md5Crypt.md5Crypt(_password.getBytes(StandardCharsets.UTF_8));
        }
        else
        if("cisco-md5crypt".equalsIgnoreCase(_hash)
                ||"cmd5".equalsIgnoreCase(_hash))
        {
            return Md5Crypt.ciscoMd5Crypt(_password);
        }
        else
        if("apr1crypt".equalsIgnoreCase(_hash)
                ||"aprcrypt".equalsIgnoreCase(_hash)
                ||"apr1".equalsIgnoreCase(_hash)
                ||"apr".equalsIgnoreCase(_hash))
        {
            return Md5Crypt.apr1Crypt(_password);
        }
        else
        if("sha1crypt".equalsIgnoreCase(_hash)
                ||"sha1".equalsIgnoreCase(_hash))
        {
            return Sha1Crypt.crypt(_password);
        }
        else
        if("sha256crypt".equalsIgnoreCase(_hash)
                ||"sha256".equalsIgnoreCase(_hash))
        {
            return Sha2Crypt.sha256Crypt(_password.getBytes(StandardCharsets.UTF_8));
        }
        else
        if("sha512crypt".equalsIgnoreCase(_hash)
            ||"sha512".equalsIgnoreCase(_hash))
        {
            return Sha2Crypt.sha512Crypt(_password.getBytes(StandardCharsets.UTF_8));
        }
        else
        if("sha1hex".equalsIgnoreCase(_hash))
        {
            return HashUtil.sha1Hex(_password);
        }
        else
        if("sha256hex".equalsIgnoreCase(_hash) )
        {
            return HashUtil.sha256Hex(_password);
        }
        else
        if("sha512hex".equalsIgnoreCase(_hash) )
        {
            return HashUtil.sha512Hex(_password);
        }
        else
        if("mysql".equalsIgnoreCase(_hash) )
        {
            return Mysql4Crypt.crypt(_password);
        }
        else
        {
            return passwordSaltToHash(_password, _hash);
        }
    }

    public static String generatePassword(String _seed, List<String> _sets, int _len)
    {
        ArcRand _rng = ArcRand.from(_seed);
        return generatePassword(_rng, _sets, _len);
    }

    public static String generatePassword(List<String> _sets, int _len)
    {
        return generatePassword(UUID.randomUUID().toString(), _sets, _len);
    }

    public static String generatePassword(int _len)
    {
        return generatePassword(UUID.randomUUID().toString(), _BASIC_SETS.get("default"), _len);
    }

    public static String generatePassword(String _seed, String _setn, int _len)
    {
        ArcRand _rng = ArcRand.from(_seed);
        return generatePassword(_rng, _BASIC_SETS.get(_setn), _len);
    }

    public static String generatePassword(ArcRand _rng, List<String> _sets, int _len)
    {
        int _row = 16;

        if(_sets == null || _sets.size() == 0)
        {
            _sets = Collections.unmodifiableList(_BASIC_SETS.get("default"));
        }

        for(String _set : _sets)
        {
            if(_row<_set.length()) _row = _set.length();
        }

        StringBuilder _sb = new StringBuilder();
        for(String _set : _sets)
        {
            ArcRand _r = ArcRand.from(_rng.next32(), _row);

            for(int _i : _r._ctx)
            {
                int _off = _i%_set.length();
                _sb.append(_set.substring(_off,_off+1));
            }
        }

        StringBuilder _sb2 = new StringBuilder();
        char _last = 0;
        int _l2 = _sb.length();
        for(int _j =0; _j< _len; _j++)
        {
            char _that = _sb.charAt(_rng._ctx[_j%_rng._ctx.length]%_l2);
            if((_j>0) && (_j%_rng._ctx.length)==(_rng._ctx.length-1))
            {
                for(int _i =0; _i< _rng._ctx.length; _i++)
                    _rng.next32();
            }
            if(_last == _that)
            {
                _len++;
                continue;
            }
            _sb2.append(_that);
            _last = _that;
        }
        return _sb2.toString();
    }

    public static String generatePassword(byte[] _buf, List<String> _sets, int _len)
    {
        if(_sets == null || _sets.size() == 0)
        {
            _sets = Collections.unmodifiableList(_BASIC_SETS.get("default"));
        }

        int _offset = ((_buf[0]&0xff)>>>2);
        char _last = 0;
        StringBuilder _sb = new StringBuilder();
        for(int _j =0; _j< _len; _j++)
        {
            int _sidx = (_offset+((_buf[_j%_buf.length]&0xff)>>>5))%_sets.size();
            char[] _set = _sets.get(_sidx).toCharArray();
            int _i = (_buf[_j%_buf.length]&0xff);
            char _that = _set[_i % _set.length];
            _offset++;
            if((_last == _that) || ((_last+1) == _that))
            {
                _len++;
                continue;
            }
            _sb.append(_last = _that);
        }
        return _sb.toString();
    }

    public static byte[] hash(String _name, byte[]... _buffer)
    {
        try {
            MessageDigest _md = MessageDigest.getInstance(_name);
            for(byte[] _b : _buffer)
            {
                _md.update(_b);
            }
            return _md.digest();
        }
        catch (NoSuchAlgorithmException e)
        {
            return null;
        }
    }

    public static byte[] hash(byte[]... _buffer)
    {
        return hash("SHA1", _buffer);
    }

    public static byte[] hash(String _buffer)
    {
        return hash("SHA1", _buffer.getBytes(StandardCharsets.UTF_8));
    }
    public static byte[] hash(String _hash,String _buffer)
    {
        return hash(_hash, _buffer.getBytes(StandardCharsets.UTF_8));
    }

    @SneakyThrows
    public static void main(String[] args)
    {
        if(args.length==0
                || "list".equalsIgnoreCase(args[0])
                || "-list".equalsIgnoreCase(args[0])
                || "-h".equalsIgnoreCase(args[0])
                || "help".equalsIgnoreCase(args[0])
                || "-help".equalsIgnoreCase(args[0])
                || "--help".equalsIgnoreCase(args[0]))
        {
            System.out.println("pwtool [-o <outfile>] 'hash' <algo> <string>");
            System.out.println("pwtool [-o <outfile>] 'crypt' <algo> <string> [<salt>]");
            System.out.println("pwtool [-o <outfile>] 'pw' <string> <len> <set> [<setfile>]");
            System.out.println("pwtool [-o <outfile>] 'pwgen' <len> <set> [<setfile>]");
            System.out.println("\tcrypt algo is any of:");
            System.out.println("\t\tbcrypt | blowfish | bfish | bf");
            System.out.println("\t\tntcrypt | nt");
            System.out.println("\t\tmd5crypt | md5");
            System.out.println("\t\tcisco-md5crypt | cmd5");
            System.out.println("\t\tapr1crypt | aprcrypt | apr1 | apr");
            System.out.println("\t\tsha1crypt | sha1");
            System.out.println("\t\tsha256crypt | sha256");
            System.out.println("\t\tsha512crypt | sha512");
            System.out.println("\t\tsha1hex | sha256hex | sha512hex");
            System.out.println("\t\tmysql");
            System.out.println("\thash algo is any of:");
            for(String _md : Security.getAlgorithms("MessageDigest"))
            {
                System.out.println("\t\t"+_md);
            }
            System.exit(0);
        }

        if("dump".equalsIgnoreCase(args[0])
            || "-dump".equalsIgnoreCase(args[0]))
        {
            StringWriter _sw = new StringWriter();
            LdataUtil.writeTo(_BASIC_SETS, _sw, true);
            System.out.println(_sw.getBuffer().toString());
        }
        else
        if("hash".equalsIgnoreCase(args[0]))
        {
            doHash(args);
        }
        else
        if("pwgen".equalsIgnoreCase(args[0]))
        {
            doPwgen(args);
        }
        else
        if("pw".equalsIgnoreCase(args[0]))
        {
            doPw(args);
        }
        else
        if("crypt".equalsIgnoreCase(args[0]))
        {
            doCrypt(args);
        }
    }


    @SneakyThrows
    static void doHash(String[] args)
    {
        if((args.length == 5) && "-o".equalsIgnoreCase(args[1]))
        {
            Files.write(new File(args[2]).toPath(), CommonUtil.toHex(hash(args[3], args[4])).getBytes(StandardCharsets.UTF_8));
        }
        else
        if((args.length == 6) && "-o".equalsIgnoreCase(args[1]))
        {
            byte[] _b = hash(args[1], args[2]);
            if("b32".equalsIgnoreCase(args[3]))
            {
                Files.write(new File(args[2]).toPath(), CommonUtil.toBase32(_b).getBytes(StandardCharsets.UTF_8));
            }
            else
            if("b64".equalsIgnoreCase(args[3]))
            {
                Files.write(new File(args[2]).toPath(), CommonUtil.toBase64Np(_b).getBytes(StandardCharsets.UTF_8));
            }
            else
            {
                Files.write(new File(args[2]).toPath(), CommonUtil.toHex(_b).getBytes(StandardCharsets.UTF_8));
            }
        }
        else
        if(args.length == 3 || args.length == 4)
        {
            byte[] _b = hash(args[1], args[2]);
            if((args.length == 4) && ("b16".equalsIgnoreCase(args[3]) || "hex".equalsIgnoreCase(args[3])))
            {
                System.out.println(CommonUtil.toHex(_b));
            }
            else
            if((args.length == 4) && "b32".equalsIgnoreCase(args[3]))
            {
                System.out.println(CommonUtil.toBase32(_b));
            }
            else
            if((args.length == 4) && "b64".equalsIgnoreCase(args[3]))
            {
                System.out.println(CommonUtil.toBase64Np(_b));
            }
            else
            {
                System.out.println("b16: "+CommonUtil.toHex(_b));
                System.out.println("b32: "+CommonUtil.toBase32(_b));
                System.out.println("b64: "+CommonUtil.toBase64Np(_b));
            }
        }
        else
        {
            System.out.println("!");
        }
    }

    @SneakyThrows
    static void doPwgen(String[] args)
    {
        if(args.length == 2 && "-list".equalsIgnoreCase(args[1]))
        {
            for(String _k : _BASIC_SETS.keySet())
            {
                System.out.println(_k);
            }
        }
        else
        if((args.length == 4) && "-o".equalsIgnoreCase(args[1]))
        {
            Files.write(new File(args[2]).toPath(), generatePassword(CommonUtil.checkInt(args[3])).getBytes(StandardCharsets.UTF_8));
        }
        else
        if(args.length == 2)
        {
            System.out.println(generatePassword(CommonUtil.checkInt(args[1])));
        }
        else
        if(args.length == 3 && "-list".equalsIgnoreCase(args[1]))
        {
            readSetFile(args[2]);
            for(String _k : _BASIC_SETS.keySet())
            {
                System.out.println(_k);
            }
        }
        else
        if((args.length == 5) && "-o".equalsIgnoreCase(args[1]))
        {
            Files.write(new File(args[2]).toPath(), generatePassword(_BASIC_SETS.get(args[4]), CommonUtil.checkInt(args[3])).getBytes(StandardCharsets.UTF_8));
        }
        else
        if(args.length == 3)
        {
            System.out.println(generatePassword(_BASIC_SETS.get(args[2]), CommonUtil.checkInt(args[1])));
        }
        else
        if((args.length == 6) && "-o".equalsIgnoreCase(args[1]))
        {
            readSetFile(args[5]);
            Files.write(new File(args[2]).toPath(), generatePassword(_BASIC_SETS.get(args[4]), CommonUtil.checkInt(args[3])).getBytes(StandardCharsets.UTF_8));
        }
        else
        if(args.length == 4)
        {
            readSetFile(args[3]);
            System.out.println(generatePassword(_BASIC_SETS.get(args[2]), CommonUtil.checkInt(args[1])));
        }
        else
        {
            for(String _k : _BASIC_SETS.keySet())
            {
                System.out.println(_k);
            }
        }
    }
    @SneakyThrows
    static void doPw(String[] args)
    {
        if(args.length == 2 && "-list".equalsIgnoreCase(args[1]))
        {
            for(String _k : _BASIC_SETS.keySet())
            {
                System.out.println(_k);
            }
        }
        else
        if((args.length == 4) && "-o".equalsIgnoreCase(args[1]))
        {
            Files.write(new File(args[2]).toPath(), passwordToPassword(args[3]).getBytes(StandardCharsets.UTF_8));
        }
        else
        if(args.length == 2)
        {
            System.out.println(passwordToPassword(args[1]));
        }
        else
        if(args.length == 3 && "-list".equalsIgnoreCase(args[1]))
        {
            readSetFile(args[2]);
            for(String _k : _BASIC_SETS.keySet())
            {
                System.out.println(_k);
            }
        }
        else
        if((args.length == 3) && "-o".equalsIgnoreCase(args[1]))
        {
            Files.write(new File(args[2]).toPath(), generatePassword(16).getBytes(StandardCharsets.UTF_8));
        }
        else
        if((args.length == 5) && "-o".equalsIgnoreCase(args[1]))
        {
            Files.write(new File(args[2]).toPath(), passwordToPassword(args[3], CommonUtil.checkInt(args[4])).getBytes(StandardCharsets.UTF_8));
        }
        else
        if(args.length == 3)
        {
            System.out.println(passwordToPassword(args[1], CommonUtil.checkInt(args[2])));
        }
        else
        if((args.length == 6) && "-o".equalsIgnoreCase(args[1]))
        {
            Files.write(new File(args[2]).toPath(), passwordToPassword(args[3], _BASIC_SETS.get(args[5]), CommonUtil.checkInt(args[4])).getBytes(StandardCharsets.UTF_8));
        }
        else
        if(args.length == 4)
        {
            System.out.println(passwordToPassword(args[1], _BASIC_SETS.get(args[3]), CommonUtil.checkInt(args[2])));
        }
        else
        if((args.length == 7) && "-o".equalsIgnoreCase(args[1]))
        {
            readSetFile(args[6]);
            Files.write(new File(args[2]).toPath(), passwordToPassword(args[3], _BASIC_SETS.get(args[5]), CommonUtil.checkInt(args[4])).getBytes(StandardCharsets.UTF_8));
        }
        else
        if(args.length == 5)
        {
            readSetFile(args[4]);
            System.out.println(passwordToPassword(args[1], _BASIC_SETS.get(args[3]), CommonUtil.checkInt(args[2])));
        }
        else
        {
            System.out.println(generatePassword(16));
        }
    }

    @SneakyThrows
    static void doCrypt(String[] args)
    {
        if((args.length == 4) && "-o".equalsIgnoreCase(args[1]))
        {
            Files.write(new File(args[2]).toPath(), passwordToHash("", args[3]).getBytes(StandardCharsets.UTF_8));
        }
        else
        if(args.length == 2)
        {
            System.out.println(passwordToHash("", args[1]));
        }
        else
        if((args.length == 5) && "-o".equalsIgnoreCase(args[1]))
        {
            Files.write(new File(args[2]).toPath(), passwordToHash(args[4], args[3]).getBytes(StandardCharsets.UTF_8));
        }
        else
        if(args.length == 3)
        {
            System.out.println(passwordToHash(args[2], args[1]));
        }
        else
        if((args.length == 6) && "-o".equalsIgnoreCase(args[1]))
        {
            Files.write(new File(args[2]).toPath(), passwordSaltToHash(args[4], args[5]).getBytes(StandardCharsets.UTF_8));
        }
        else
        if(args.length == 4)
        {
            System.out.println(passwordSaltToHash(args[2], args[3]));
        }
        else
        {
            System.out.println("!");
        }
    }
}
