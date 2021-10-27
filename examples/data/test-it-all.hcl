# simple comment

date-time-1 = D:2021-01-01T01:01:10.123+00:30
date-time-2 = D:2021-01-01T01:01:10.123Z
date-time-3 = D:2021-01-01T01:01:10.123
date-time-4 = D:2021-01-01T01:01:10+00:30
date-time-5 = D:2021-01-01T01:01:10Z
date-time-6 = D:2021-01-01T01:01:10
date-time-7 = D:2021-01-01T01:01+00:30
date-time-8 = D:2021-01-01T01:01Z
date-time-9 = D:2021-01-01T01:01
date-time-10 = D:2021-01-01
date-time-11 = D:2021-01-01Z
date-time-12 = D:2021-01-01+00:30
date-time-13 = D:2021-01-01-0030

date-time-14 = D:20210101010110+00:30
date-time-15 = D:20210101010110Z
date-time-16 = D:20210101010110
date-time-17 = D:202101010101+00:30
date-time-18 = D:202101010101Z
date-time-19 = D:202101010101
date-time-20 = D:20210101
date-time-21 = D:20210101Z
date-time-22 = D:20210101+00:30
date-time-23 = D:20210101-0030

from-to = [ D:2021-01-01 D:2021-01-31 ]

/* simple struct */
struct1 {
this1_key = << EOS
                 this
                is
                    problematic
              text
EOS
this2_key = << EOS this
                is
                    problematic
              text
EOS

this3_key = << EOS
               this\t
                is \xff=\xFF
                    problematic \u00FF=\u00ff
              text
EOS
}

// complex struct
struct2 = {
    string_array = [ one two "three" ]
    int_array = [  1 2 3 ]
    float_array = [ 0.1 0.2 0.3 ]
    long_text = << EOS
                    very long text
EOS
    complex_array = [ 1 .2 "three" 0x4 five ]
}

struct2 struct_array {
#            "hex1_key" = 0xdeadbeef
#            hex2_key = 0xcafeaffe
#            hex3_key = 0xf
#            hex4_key = 0xff
#            hex5_key = 0xfff
#            hex6_key = 0x0123456789abcdef
#            hex7_key = 0x0123_4567_89ab_cdef
        }
struct2 struct_array {
            dec1_key = 0
            dec2_key = 0315
        }
struct2 struct_array {
#            binary1_string = 0b010001000100010101010100010100101
#            binary2_string = 0b0_10001000_10001010_10101000_10100101
        }
struct2 struct_array {
            float1_key = 0.1
            float2_key = .1
#            float3_key = .12345_67890_12345_67
        }
struct2 struct_array {
            "1" = "one"       "2" = "two"       "3" = "three"
        }


task = [
{
name = 1
some = 2
}
{
name = 3
some = 4
}
]

EVENT "1.3.6.1.4.1.9.*" {
    match = "regexp"
    script = << _EOS_
    some script
_EOS_
}

EVENT "1.3.6.1.4.1.9.*" {
    match = "regexp"
    script = << _EOS_
    some script
_EOS_
}

EVENT "1.3.6.1.4.1.666.*" {
    match = "regexp"
    map {
     "1" = 2
    }
    script = << _EOS_
    some script
_EOS_
}
