package com.github.terefang.jmelange.passwd.crypt;

import com.github.terefang.jmelange.commons.CommonUtil;
import lombok.SneakyThrows;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Pattern;

// Crypt-Juniper/source/lib/Crypt/Juniper.pm
public class JunOsCrypt {
    public static String MAGIC = "$9$";
    private static final String itoa64 = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static Random rnd = new Random();

    // Constants for encrypt9 method
    private static final String[] FAMILY = {"QzF3n6/9CAtpu0O", "B1IREhcSyrleKvMW8LXx", "7N-dVbwsY2g4oaJZGUDj", "iHkq.mPf5T"};
    private static Map<String,Integer> EXTRA = new HashMap<String,Integer> ();
    private static Pattern VALID;
    private static final String[] NUM_ALPHA;
    private static Map<String,Integer> ALPHA_NUM = new HashMap<String,Integer> ();
    private static final int[][] ENCODING = {
            {1,  4, 32},
            {1, 16, 32},
            {1,  8, 32},
            {1, 64    },
            {1, 32    },
            {1,  4, 16, 128},
            {1, 32, 64}
    };

    static {
        // Prepare EXTRA constant for use with encrypt9 method
        for (int fam = 0; fam < FAMILY.length; fam++) {
            for (char c : FAMILY[fam].toCharArray()) {
                EXTRA.put(String.valueOf(c), 3 - fam);
            }
        }

        // Prepare VALID RegEx pattern constant for use with decrypt9 method (VALID currently not used as decrypt9 has not been added to class but letters and end variables are used in next section)
        StringBuilder letters = new StringBuilder("");
        for (String item : FAMILY) {
            letters.append(item);
        }
        String end = letters.toString() + "{4,}$";
        end.replace("-", "\\-");
        VALID = Pattern.compile("^\\Q" + MAGIC + "\\E" + end);

        // Prepare NUM_ALPHA and ALPHA_NUM constants for use with encrypt9 method
        NUM_ALPHA = new String[letters.length()];
        int x = 0;
        for (char item : letters.toString().toCharArray()) {
            NUM_ALPHA[x] = String.valueOf(item);
            x++;
        }
        for (int num = 0; num < NUM_ALPHA.length; num++) {
            ALPHA_NUM.put(NUM_ALPHA[num], num);
        }
    }

    /**
     * Creates a reversable $9 password used for most passwords in Junos configurations.
     *
     * @param pw Password to encrypt.
     * @return String - Encrypted password.  Can be null if encryption fails.
     */
    public static String crypt(String pw)
    {
        //String salt = randomSalt(1);
        int _pos = (rnd.nextInt()&0x7ffffff)%FAMILY[0].length();
        String salt = FAMILY[0].substring(_pos,_pos+1);
        String rand = randomSalt(EXTRA.get(salt));
        return  encrypt9(pw, salt, rand);
    }

    public static String encrypt9(String pw, String salt, String rand)
    {
        int pos = 0;
        String prev = salt;
        String crypt = MAGIC + salt + rand;

        for (char item : pw.toCharArray()) {
            int[] encode = ENCODING[pos % ENCODING.length];
            crypt += gapEncode(item, prev, encode);
            prev = crypt.substring(crypt.length() - 1);
            pos++;
        }

        return crypt;
    }

    public static String crypt(String pw, String _enc)
    {
        // is $9$...
        String salt = _enc.substring(3,4);
        String rand = _enc.substring(4,4+EXTRA.get(salt));
        return  encrypt9(pw, salt, rand);
    }

    public static boolean checkpw(String pw, String _enc)
    {
        return crypt(pw, _enc).equals(_enc);
    }

    /**
     * Helper function to generate random salt strings.
     *
     * @param len Length of the random salt to create.
     * @return String - Generated salt string.
     */
    private static String randomSalt(int len)
    {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
        {
            sb.append(itoa64.charAt(rnd.nextInt(itoa64.length())));
        }
        return sb.toString();
    }

    /**
     * Conversion method that is part of the FreeBSD crypt algorithm.  Not designed to be called except by {@link #crypt(String, String)}.
     */
    private static String to64(long v, int n)
    {
        String output = "";

        while (--n >= 0)
        {
            output += itoa64.charAt((int) (v & 0x3f));
            v >>=6;
        }

        return (output);
    }

    /**
     * Helper function for {@link #encrypt9(String)} method.  Generates part of the encrypted string and returns it back to the {@link #encrypt9(String)} method.
     *
     * @param pc Character to encrypt.
     * @param prev Previous series of encrypted characters or salt.
     * @param enc Current encoder values.
     * @return String - Encrypted version of the character (can be 2 to 4 characters).
     */
    private static String gapEncode(char pc, String prev, int[] enc) {
        String crypt = "";
        int ord = (int) pc;
        Deque<Integer> gaps = new LinkedList<Integer>();

        for (int x = enc.length - 1; x >= 0; x--) {
            gaps.addFirst(ord / enc[x]);
            ord %= enc[x];
        }

        for (Integer item : gaps) {
            item += ALPHA_NUM.get(prev) + 1;
            String c = prev = NUM_ALPHA[item % NUM_ALPHA.length];
            crypt += c;
        }

        return crypt;
    }

    @SneakyThrows
    public static void main(String[] args) {
        for(String[] _vector : _TEST)
        {
            System.err.println(JunOsCrypt.checkpw(_vector[1], _vector[0]));
            System.err.println(JunOsCrypt.crypt(_vector[1]));
        }
    }

    public static String[][] _TEST = {
            {"$9$z8Hx36CEhy", "EJ", "8Hx"},
            {"$9$2DoJDF39", "fm", "D"},
            {"$9$.fz6u0I", "nf", ""},
            {"$9$F4yB/CuOBE", "ga", "4yB"},
            {"$9$e0FKWxZGi", "ho", "0F"},
            {"$9$JqUHmCA0", "nl", "q"},
            {"$9$K0vWX74oJUjH", "***", "0v"},
            {"$9$6aMR/tOcSe", "lf", "aMR"},
            {"$9$nSHx9AuIES", "FT", "SHx"},
            {"$9$Oyq6RSeLxd", "ou", "yq6"},
            {"$9$QrU-FCpSyl8x-.P", "U*UU", "rU-"},
            {"$9$Ql/lznCuOR", "hs", "l/l"},
            {"$9$hrFreMbsg", "GZ", "rF"},
            {"$9$LE0x7VgoG", "au", "E0"},
            {"$9$yHfreW4oG", "do", "Hf"},
            {"$9$KIbvX-4aU", "px", "Ib"},
            {"$9$fQnCEcl", "nz", ""},
            {"$9$yK1K87oaU", "ok", "K1"},
            {"$9$8Ah7-wY2a", "ga", "Ah"},
            {"$9$xxr-s4Di.", "rv", "xr"},
            {"$9$nGZJ/A0Ecl", "iu", "GZJ"},
            {"$9$Rpahrv8LN", "mb", "pa"},
            {"$9$oZJUH/9p", "in", "Z"},
            {"$9$EqOhSeKMX", "dp", "qO"},
            {"$9$J9Djq5T3", "cd", "9"},
            {"$9$jSHmT9Cu", "mg", "S"},
            {"$9$qP39", "z", ""},
            {"$9$/YxPCOI", "U", "YxP"},
            {"$9$69K19tOlK8", "iz", "9K1"},
            {"$9$uAtEOEyreW", "qp", "AtE"},
            {"$9$wTYgJPfz", "ek", "T"},
            {"$9$UYDi.zF/", "df", "Y"},
            {"$9$OlVGBhr8X-", "pv", "lVG"},
            {"$9$OSsS1IcvWx", "av", "SsS"},
            {"$9$7n-dsg4Z", "ab", "n"},
            {"$9$-1dw2jiq", "HI", "1"},
            {"$9$nX/./CpyrK", "EK", "X/."},
            {"$9$i.PT9Au", "GW", ""},
            {"$9$CjaCtuByrK", "EG", "jaC"},
            {"$9$m5T3n/t", "bp", ""},
            {"$9$qPz6u0I", "rf", ""},
            {"$9$TQnCuOR", "ls", ""},
            {"$9$WHIXxdgoG", "bv", "HI"},
            {"$9$3HI969tcye", "EZ", "HI9"},
            {"$9$JcZUH5Qn", "dv", "c"},
            {"$9$RWRSyKVb2", "ck", "WR"},
            {"$9$P5QnBIc", "ez", ""},
            {"$9$tgHtp1hrlM", "pc", "gHt"},
            {"$9$/KR/9t0B1E", "HA", "KR/"},
            {"$9$uYMFB1hleW", "bd", "YMF"},
            {"$9$8KmL7d2ga", "HE", "Km"},
            {"$9$peBWOEyvWx", "rt", "eBW"},
            {"$9$GtD.fCApIhy", "U*U", "t"},
            {"$9$FwgH36A0OR", "dc", "wgH"},
            {"$9$5Q39CtO", "ip", ""},
            {"$9$EjvSyK7-w", "bw", "jv"},
            {"$9$guoaUQz6", "al", "u"},
            {"$9$ZKUHmfT3", "mq", "K"},
            {"$9$40JZjkqfQz6", "bbb", "0"},
            {"$9$DPiqf36A", "iu", "P"},
            {"$9$aYUDHmPT", "CC", "Y"},
            {"$9$.f5FIRS", "bn", ""},
            {"$9$Np-bYoaG", "HC", "p"},
            {"$9$l29eMXNds", "hr", "29"},
            {"$9$eucvX-g4Z", "qg", "uc"},
            {"$9$8S9L7VZGi", "hj", "S9"},
            {"$9$X7Z-VYDjq", "gi", "7Z"},
            {"$9$1G0EclVb2", "fm", "G0"},
            {"$9$yAAe87db2", "rr", "AA"},
            {"$9$3sVe9AuEhy", "GF", "sVe"},
            {"$9$Wqdx7Vs2a", "cr", "qd"},
            {"$9$uSA4Ohyx7-bs2z3", "U**U", "SA4"},
            {"$9$he1rlvx7d", "CE", "e1"},
            {"$9$uNnu1hrKvL", "ob", "Nnu"},
            {"$9$CUhXpuBSye", "BF", "UhX"},
            {"$9$E2ESyeM8x", "BR", "2E"},
            {"$9$R8USeW2gJ", "om", "8U"},
            {"$9$TzF69Cp0OIEc", "AAAA", ""},
            {"$9$pvn.0OIEhyleM8X7dbw4o", "AAAAAA", "vn."},
            {"$9$pvxuO1EvWX", "FX", "vxu"},
            {"$9$1YZRyK8X-", "qs", "YZ"},
            {"$9$5Fn9O1E", "GV", ""},
            {"$9$gC4Gi5T3", "pg", "C"},
            {"$9$JQDjk5Q3", "CU", "Q"},
            {"$9$FZ/a/Cphcr", "GH", "Z/a"},
            {"$9$7gdYoHkP", "rh", "g"},
            {"$9$gzoUHQF/", "qx", "z"},
            {"$9$jrkmTn6A", "jd", "r"},
            {"$9$CYpfApBevL", "dz", "Ypf"},
            {"$9$PfF/uOR", "pu", ""},
            {"$9$VAYoGHqf", "ot", "A"},
            {"$9$rQalWx-VY", "pr", "Qa"},
            {"$9$O8LcBIhKML", "DV", "8Lc"},
            {"$9$j/ikm9Cp", "DK", "/"},
            {"$9$zA.-6/AOBR", "CD", "A.-"},
            {"$9$O6ISRSe7Nb", "oh", "6IS"},
            {"$9$iq5Fp0I", "rx", ""},
            {"$9$zeh536CuOI", "ES", "eh5"},
            {"$9$JZGHm3nC", "qg", "Z"},
            {"$9$5F391Ic", "ch", ""},
            {"$9$z57.36Cu01", "EC", "57."},
            {"$9$/BLgC0IlK8", "qw", "BLg"},
            {"$9$m539IEy", "ry", ""},
            {"$9$2g4aU.mT", "ef", "g"},
            {"$9$kmPT6/A", "BE", ""},
            {"$9$kqmTtu1", "dz", ""},
            {"$9$ZMUi.69p", "iz", "M"},
            {"$9$mP5zAt0", "DG", ""},
            {"$9$V.wsgoaGDjk.P5z3nAt", "AAAAAA", "."},
            {"$9$0Nyg1yKdb2oGiJG", "zzzz", "Nyg"},
            {"$9$5QF6At0", "EC", ""},
            {"$9$I2BEclKvL", "ea", "2B"},
            {"$9$xfT-wg", "n", "fT"},
            {"$9$TQnCApB", "lp", ""},
            {"$9$eptM8xJZD", "FN", "pt"},
            {"$9$pf1KOEyevL", "rr", "f1K"},
            {"$9$o-ZGiq.5zF/wY4Z", "bbbbb", "-"},
            {"$9$sb2oGiHm", "ic", "b"},
            {"$9$bvs2oiH.", "EG", "v"},
            {"$9$fzF/cSe", "cn", ""},
            {"$9$7xVbY4oZ", "CB", "x"},
            {"$9$cihSrvNds", "dw", "ih"},
            {"$9$qPQn", "n", ""},
            {"$9$26aZjkqf", "gb", "6"},
            {"$9$EhDcyKdVY", "ej", "hD"},
            {"$9$84i7-wg4Z", "gc", "4i"},
            {"$9$qmfQ1Ih", "EO", ""},
            {"$9$OXmoIRSWLN", "bw", "Xmo"},
            {"$9$T39p1RS", "ot", ""},
            {"$9$Tz/tuOR", "qq", ""},
            {"$9$O3-vIEyN-w", "fl", "3-v"},
            {"$9$i.5F1Ic", "om", ""},
            {"$9$PQF/tpB", "gc", ""},
            {"$9$oHJjqAtO", "qo", "H"},
            {"$9$eZPW8xZGj", "CO", "ZP"},
            {"$9$vf-Lds", "w", "f-"},
            {"$9$LOu7NVgoZ", "BU", "Ou"},
            {"$9$EgRcyevML", "EA", "gR"},
            {"$9$.mfz/Cu", "dt", ""},
            {"$9$APVG0ORcyK", "cr", "PVG"},
            {"$9$tvb00IcLxd", "nz", "vb0"},
            {"$9$C7gUp1hvMX", "rg", "7gU"},
            {"$9$vk187VgoG", "nu", "k1"},
            {"$9$.mfzn/t", "dr", ""},
            {"$9$pIoxuBEXxd", "hm", "Iox"},
            {"$9$.fTF0OI", "FJ", ""},
            {"$9$Y.4Giq.5", "rb", "."},
            {"$9$mf5FAtO", "af", ""},
            {"$9$iqmT0OR", "fm", ""},
            {"$9$APNN0OR8LN", "cl", "PNN"},
            {"$9$2ZoaUf5F", "bi", "Z"},
            {"$9$rC-KML4oZ", "FM", "C-"},
            {"$9$g94aGqm5", "DV", "9"},
            {"$9$Xpj7NVwsgoaGDikmf53n", "AAAAAA", "pj"},
            {"$9$5QF6hcr", "EN", ""},
            {"$9$Z.Gjq/9p", "hl", "."},
            {"$9$0e5n1hr8LN", "nf", "e5n"},
            {"$9$T39pREy", "of", ""},
            {"$9$OJo4BRSrlM", "ha", "Jo4"},
            {"$9$oiaZjikP", "dp", "i"},
            {"$9$nBOF6tOSrv", "pw", "BOF"},
            {"$9$qfz69Cu", "oa", ""},
            {"$9$HmPTApO", "CY", ""},
            {"$9$rZCKv8s2o", "BZ", "ZC"},
            {"$9$.PfzFnC", "ap", ""},
            {"$9$mf3/IRheMLtp", "U*U*", ""},
            {"$9$cBslK8xNb", "gr", "Bs"},
            {"$9$rNOvLN-VY", "op", "NO"},
            {"$9$9QuDtORW87", "nk", "QuD"},
            {"$9$9vDhtBEvWx", "rx", "vDh"},
            {"$9$H.m5n/A", "BU", ""},
            {"$9$SEuleWN-w", "be", "Eu"},
            {"$9$M3aWXNdVs", "HA", "3a"},
            {"$9$R54cSlVbY", "BM", "54"},
            {"$9$.5F/uOR", "ou", ""},
            {"$9$6xRTApOSrK", "GW", "xRT"},
            {"$9$daVs4k.5", "hz", "a"},
            {"$9$.fz6OBE", "nh", ""},
            {"$9$4HJDkzF/", "nh", "H"},
            {"$9$c62yMLdbY", "UU", "62"},
            {"$9$N3ds4UDk", "me", "3"},
            {"$9$U8jqfTQn", "ma", "8"},
            {"$9$.5T3", "c", ""},
            {"$9$DfjHmtpB", "dn", "f"},
            {"$9$KmXWX-2gJ", "jf", "mX"},
            {"$9$jXkPQu0I", "nk", "X"},
            {"$9$qmPQ3nC", "ab", ""},
            {"$9$4oJUHzF/", "ji", "o"},
            {"$9$6V-lAORvMX", "si", "V-l"},
            {"$9$kP5z/Cp", "GT", ""},
            {"$9$UKHkm36C", "CW", "K"},
            {"$9$ITEhcr8XN", "BV", "TE"},
            {"$9$Hq5FuOR", "qy", ""},
            {"$9$6iNQ/tOreW", "ly", "iNQ"},
            {"$9$HmfQ0OI", "GL", ""},
            {"$9$aqZi.z39", "qv", "q"},
            {"$9$-zdYoZUH", "pr", "z"},
            {"$9$4/ZUH", "g", "/"},
            {"$9$gTJGjk.f", "GR", "T"},
            {"$9$NGV2aHqf", "rw", "G"},
            {"$9$.PT3B1h", "ik", ""},
            {"$9$kmPT/9t", "BF", ""},
            {"$9$qmfzAtO", "eg", ""},
            {"$9$ZIUi.TQn", "id", "I"},
            {"$9$qfz6cSe", "oo", ""},
            {"$9$d.sY4Diq", "CV", "."},
            {"$9$sQ24JjH.", "ET", "Q"},
            {"$9$qPfzp0I", "by", ""},
            {"$9$dbsaU", "w", "b"},
            {"$9$.539REy", "sj", ""},
            {"$9$.5Q3B1E", "GK", ""},
            {"$9$mPQnpu1", "lf", ""},
            {"$9$QOO636Cp01", "FR", "OO6"},
            {"$9$dXwY4Hkm", "FI", "X"},
            {"$9$cETyrv-VY", "ax", "ET"},
            {"$9$62q0ApBlK8", "gy", "2q0"},
            {"$9$80Tx7dDjk", "BN", "0T"},
            {"$9$fT39REy", "mj", ""},
            {"$9$dIVs4ZGi", "hc", "I"},
            {"$9$PTz6tu1", "ft", ""},
            {"$9$PTQnhcl", "bo", ""},
            {"$9$O4DcREylK8", "cq", "4Dc"},
            {"$9$T39pIRS", "oe", ""},
            {"$9$YsoaUHqf", "cs", "s"},
            {"$9$1GGREyleWLX-/CtO1IrlKM8XxdbwgoaUcylM8LVbs24aJDiHmf5FNdb24ojik.P5T36/tu0IGDi.Pfn69Ap0ORhclKvLz36ApuEhSrevMX", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "GG"},
            {"$9$NcdVs2gaZGjHqm5Qz/9AtuOBR", "AAAAAAAA", "c"},
            {"$9$ZdjiqTQ3", "CE", "d"},
    };
}
