#!/usr/bin/perl

my $file = $ARGV[0];

open(FH, "<$file");

while(!eof(FH))
{
    my $set;
    read(FH,$set,4);
    my @cod = unpack('nn', $set);
    printf("0x%02X 0x%04X\n", @cod);
}
close(FH);