# Conflux

## Abstract

this code is based on concepts established in:

language confluxer (http://generators.christopherpound.com/)
written by Christopher Pound (pound@rice.edu), July 1993.

over the years various improvements have been made to the original functionality,
especially using java instead of the brutish perl language, 
your milage may vary.

Conflux will :

* read one or many sample text file(s) 
* build internal lookup table(s)
* generate (random) output that looks similar to the sample(s)

you can influence the output given the various options below.

the most simple usage would be:

```
java -jar conflux.jar -i <sample-file>
```

## Usage

```
java -jar conflux.jar [options]
    -T  <n> table size (def=2)
    -n  <n> min word size (def=4)
    -m  <n> max word size (def=5)
    -s  <n> number of words (def=20)
    -S  <n> random seed
    -i  <f> input file
    -d      use debug
    -b  <n> loop breaker (def=100)
    -f  <n> fudge chars (def=3)
    -r      use restricted mode (faster)
```

## License

Copyright (c) 2006-2015, Alfred Reibenschuh

All rights reserved.

Redistribution and use in source and binary forms, with or without 
modification, are permitted provided that the following conditions 
are met:

1. Redistributions of source code must retain the above copyright 
notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright 
notice, this list of conditions and the following disclaimer in the 
documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR 
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT 
HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED 
TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

