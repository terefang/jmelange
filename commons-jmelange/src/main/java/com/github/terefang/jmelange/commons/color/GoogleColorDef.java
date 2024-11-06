package com.github.terefang.jmelange.commons.color;

import com.github.terefang.jmelange.commons.util.ColorUtil;

import java.awt.Color;

public enum GoogleColorDef implements IColorDef
{
    
    BLACK                   (ColorUtil.fromRgb(0x00,0x00,0x00)),
    WHITE                   (ColorUtil.fromRgb(0xff,0xff,0xff)),
    RED_BERRY               (ColorUtil.fromRgb(0x98,0x00,0x00)),
    RED                     (ColorUtil.fromRgb(0xff,0x00,0x00)),
    ORANGE                  (ColorUtil.fromRgb(0xff,0x99,0x00)),
    YELLOW                  (ColorUtil.fromRgb(0xff,0xff,0x00)),
    GREEN                   (ColorUtil.fromRgb(0x00,0xff,0x00)),
    CYAN                    (ColorUtil.fromRgb(0x00,0xff,0xff)),
    CORNFLOWER_BLUE         (ColorUtil.fromRgb(0x4a,0x86,0xe8)),
    BLUE                    (ColorUtil.fromRgb(0x00,0x00,0xff)),
    PURPLE                  (ColorUtil.fromRgb(0x99,0x00,0xff)),
    MAGENTA                 (ColorUtil.fromRgb(0xff,0x00,0xff)),
    LIGHT_RED_BERRY_3       (ColorUtil.fromRgb(0xe6,0xb8,0xaf)),
    LIGHT_RED_3             (ColorUtil.fromRgb(0xf4,0xcc,0xcc)),
    LIGHT_ORANGE_3          (ColorUtil.fromRgb(0xfc,0xe5,0xcd)),
    LIGHT_YELLOW_3          (ColorUtil.fromRgb(0xff,0xf2,0xcc)),
    LIGHT_GREEN_3           (ColorUtil.fromRgb(0xd9,0xea,0xd3)),
    LIGHT_CYAN_3            (ColorUtil.fromRgb(0xd0,0xe0,0xe3)),
    LIGHT_CORNFLOWER_BLUE_3 (ColorUtil.fromRgb(0xc9,0xda,0xf8)),
    LIGHT_BLUE_3            (ColorUtil.fromRgb(0xcf,0xe2,0xf3)),
    LIGHT_PURPLE_3          (ColorUtil.fromRgb(0xd9,0xd2,0xe9)),
    LIGHT_MAGENTA_3         (ColorUtil.fromRgb(0xea,0xd1,0xdc)),
    LIGHT_RED_BERRY_2       (ColorUtil.fromRgb(0xdd,0x7e,0x6b)),
    LIGHT_RED_2             (ColorUtil.fromRgb(0xea,0x99,0x99)),
    LIGHT_ORANGE_2          (ColorUtil.fromRgb(0xf9,0xcb,0x9c)),
    LIGHT_YELLOW_2          (ColorUtil.fromRgb(0xff,0xe5,0x99)),
    LIGHT_GREEN_2           (ColorUtil.fromRgb(0xb6,0xd7,0xa8)),
    LIGHT_CYAN_2            (ColorUtil.fromRgb(0xa2,0xc4,0xc9)),
    LIGHT_CORNFLOWER_BLUE_2 (ColorUtil.fromRgb(0xa4,0xc2,0xf4)),
    LIGHT_BLUE_2            (ColorUtil.fromRgb(0x9f,0xc5,0xe8)),
    LIGHT_PURPLE_2          (ColorUtil.fromRgb(0xb4,0xa7,0xd6)),
    LIGHT_MAGENTA_2         (ColorUtil.fromRgb(0xd5,0xa6,0xbd)),
    LIGHT_RED_BERRY_1       (ColorUtil.fromRgb(0xcc,0x41,0x25)),
    LIGHT_RED_1             (ColorUtil.fromRgb(0xe0,0x66,0x66)),
    LIGHT_ORANGE_1          (ColorUtil.fromRgb(0xf6,0xb2,0x6b)),
    LIGHT_YELLOW_1          (ColorUtil.fromRgb(0xff,0xd9,0x66)),
    LIGHT_GREEN_1           (ColorUtil.fromRgb(0x93,0xc4,0x7d)),
    LIGHT_CYAN_1            (ColorUtil.fromRgb(0x76,0xa5,0xaf)),
    LIGHT_CORNFLOWER_BLUE_1 (ColorUtil.fromRgb(0x6d,0x9e,0xeb)),
    LIGHT_BLUE_1            (ColorUtil.fromRgb(0x6f,0xa8,0xdc)),
    LIGHT_PURPLE_1          (ColorUtil.fromRgb(0x8e,0x7c,0xc3)),
    LIGHT_MAGENTA_1         (ColorUtil.fromRgb(0xc2,0x7b,0xa0)),
    DARK_RED_BERRY_1        (ColorUtil.fromRgb(0xa6,0x1c,0x00)),
    DARK_RED_1              (ColorUtil.fromRgb(0xcc,0x00,0x00)),
    DARK_ORANGE_1           (ColorUtil.fromRgb(0xe6,0x91,0x38)),
    DARK_YELLOW_1           (ColorUtil.fromRgb(0xf1,0xc2,0x32)),
    DARK_GREEN_1            (ColorUtil.fromRgb(0x6a,0xa8,0x4f)),
    DARK_CYAN_1             (ColorUtil.fromRgb(0x45,0x81,0x8e)),
    DARK_CORNFLOWER_BLUE_1  (ColorUtil.fromRgb(0x3c,0x78,0xd8)),
    DARK_BLUE_1             (ColorUtil.fromRgb(0x3d,0x85,0xc6)),
    DARK_PURPLE_1           (ColorUtil.fromRgb(0x67,0x4e,0xa7)),
    DARK_MAGENTA_1          (ColorUtil.fromRgb(0xa6,0x4d,0x79)),
    DARK_RED_BERRY_2        (ColorUtil.fromRgb(0x85,0x20,0x0c)),
    DARK_RED_2              (ColorUtil.fromRgb(0x99,0x00,0x00)),
    DARK_ORANGE_2           (ColorUtil.fromRgb(0xb4,0x5f,0x06)),
    DARK_YELLOW_2           (ColorUtil.fromRgb(0xbf,0x90,0x00)),
    DARK_GREEN_2            (ColorUtil.fromRgb(0x38,0x76,0x1d)),
    DARK_CYAN_2             (ColorUtil.fromRgb(0x13,0x4f,0x5c)),
    DARK_CORNFLOWER_BLUE_2  (ColorUtil.fromRgb(0x11,0x55,0xcc)),
    DARK_BLUE_2             (ColorUtil.fromRgb(0x0b,0x53,0x94)),
    DARK_PURPLE_2           (ColorUtil.fromRgb(0x35,0x1c,0x75)),
    DARK_MAGENTA_2          (ColorUtil.fromRgb(0x74,0x1b,0x47)),
    DARK_RED_BERRY_3        (ColorUtil.fromRgb(0x5b,0x0f,0x00)),
    DARK_RED_3              (ColorUtil.fromRgb(0x66,0x00,0x00)),
    DARK_ORANGE_3           (ColorUtil.fromRgb(0x78,0x3f,0x04)),
    DARK_YELLOW_3           (ColorUtil.fromRgb(0x7f,0x60,0x00)),
    DARK_GREEN_3            (ColorUtil.fromRgb(0x27,0x4e,0x13)),
    DARK_CYAN_3             (ColorUtil.fromRgb(0x0c,0x34,0x3d)),
    DARK_CORNFLOWER_BLUE_3  (ColorUtil.fromRgb(0x1c,0x45,0x87)),
    DARK_BLUE_3             (ColorUtil.fromRgb(0x07,0x37,0x63)),
    DARK_PURPLE_3           (ColorUtil.fromRgb(0x20,0x12,0x4d)),
    DARK_MAGENTA_3          (ColorUtil.fromRgb(0x4c,0x11,0x30)),
    SIMPLE_LIGHT_TEXT_1       (ColorUtil.fromRgb(0x00,0x00,0x00)),
    SIMPLE_LIGHT_TEXT_2       (ColorUtil.fromRgb(0x59,0x59,0x59)),
    SIMPLE_LIGHT_BACKGROUND_1 (ColorUtil.fromRgb(0xff,0xff,0xff)),
    SIMPLE_LIGHT_BACKGROUND_2 (ColorUtil.fromRgb(0xee,0xee,0xee)),
    SIMPLE_LIGHT_ACCENT_1     (ColorUtil.fromRgb(0x42,0x85,0xf4)),
    SIMPLE_LIGHT_ACCENT_2     (ColorUtil.fromRgb(0x21,0x21,0x21)),
    SIMPLE_LIGHT_ACCENT_3     (ColorUtil.fromRgb(0x78,0x90,0x9c)),
    SIMPLE_LIGHT_ACCENT_4     (ColorUtil.fromRgb(0xff,0xab,0x40)),
    SIMPLE_LIGHT_ACCENT_5     (ColorUtil.fromRgb(0x00,0x97,0xa7)),
    SIMPLE_LIGHT_ACCENT_6     (ColorUtil.fromRgb(0xee,0xff,0x41)),
    SIMPLE_DARK_TEXT_1       (ColorUtil.fromRgb(0xff,0xff,0xff)),
    SIMPLE_DARK_TEXT_2       (ColorUtil.fromRgb(0xad,0xad,0xad)),
    SIMPLE_DARK_BACKGROUND_1 (ColorUtil.fromRgb(0x21,0x21,0x21)),
    SIMPLE_DARK_BACKGROUND_2 (ColorUtil.fromRgb(0x30,0x30,0x30)),
    SIMPLE_DARK_ACCENT_1     (ColorUtil.fromRgb(0x00,0x96,0x88)),
    SIMPLE_DARK_ACCENT_2     (ColorUtil.fromRgb(0xee,0xee,0xee)),
    SIMPLE_DARK_ACCENT_3     (ColorUtil.fromRgb(0x78,0x90,0x9c)),
    SIMPLE_DARK_ACCENT_4     (ColorUtil.fromRgb(0xff,0xab,0x40)),
    SIMPLE_DARK_ACCENT_5     (ColorUtil.fromRgb(0x4d,0xd0,0xe1)),
    SIMPLE_DARK_ACCENT_6     (ColorUtil.fromRgb(0xee,0xff,0x41));
    
    
    GoogleColorDef(Color color)
    {
        this.color = color;
    }
    
    private Color color;
    
    public Color getColor() {
        return color;
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
}
