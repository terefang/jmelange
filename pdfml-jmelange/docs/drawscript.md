# Draw Script for pmltopdf

## Global Commands

* `settitle _text`
* `setcreator _text`
* `setauthor _text`
* `setsubject _text`
* `setkeywords _text`
* `setproducer _text`

* `loadfont _id, _src`
* `loadfont _id, _src, _cs`
* `loadfont _id, _src, _cs, _im`

## Page Commands

* `newpage`
* `newcontent`
* `mediabox _a, _b`
* `mediabox _a, _b, _c, _d`

## Content Commands

* `gsave`
* `grestore`
* `startlayer _name`
* `endlayer`
* `flatness _i`
* `meterlimit _d`
* `linejoin _i`
* `linecap _i`
* `linedash _d0, ..., _dN`
* `linedashx _off, _d0, ..., _dN`
* `linewidth _d`
* `pen _color, _wd`
* `pen _color, _wd, _a`

## Transformation  Commands

* `matrix  _a, _b, _c, _d, _e, _f`
* `rotate _r`
* `skew _sx, _sy`
* `scale _sx, _sy`
* `translate _x, _y`

## Color Commands

* `fillalpha _pct`
* `strokealpha _pct`

* `fillcolor _colorspec`
* `fillcolor 'gray' _d`
* `fillcolor 'gray%' _pct`
* `fillcolor 'rgb' _d _d _d`
* `fillcolor 'rgb%' _pct _pct _pct`
* `fillcolor 'hsl%' _pct _pct _pct`
* `fillcolor 'hsv%' _pct _pct _pct`
* `fillcolor 'hwb%' _pct _pct _pct`
* `fillcolor 'cmyk' _i _i _i _i`
* `fillcolor 'cmyk%' _pct _pct _pct _pct`
* `fillcolor _pct _pct _pct`
* `fillcolor _pct _pct _pct _pct`

* `strokecolor _colorspec`
* `strokecolor 'gray' _d`
* `strokecolor 'gray%' _pct`
* `strokecolor 'rgb' _d _d _d`
* `strokecolor 'rgb%' _pct _pct _pct`
* `strokecolor 'hsl%' _pct _pct _pct`
* `strokecolor 'hsv%' _pct _pct _pct`
* `strokecolor 'hwb%' _pct _pct _pct`
* `strokecolor 'cmyk' _i _i _i _i`
* `strokecolor 'cmyk%' _pct _pct _pct _pct`
* `strokecolor _pct _pct _pct`
* `strokecolor _pct _pct _pct _pct`

### Gfx Commands

* `moveto _x _y`
* `moverel _dx _dy`
* `movepolar _r _p`
* `movetox _x`
* `movetoy _y`
* `line x0, y0, x1,y1`
* `lineto x1,y1`
* `hline x1`
* `vline y1`
* `closepath`
* `endpath`

* `arc _x, _y, _a, _b, _alpha, _beta`
* `arc _x, _y, _a, _b, _alpha, _beta _move`
* `arrow _x0 _y0 _x1 _y1 _scale _from _to`
* `arrowto _x1 _y1 _scale _from _to`
* `arrowrel _dx _dy _scale _from _to`
* `arrowpolar _d _alpha _scale _from _to`
* `rect _x0 _y0 _w _h`
* `rectxy _x0 _y0 _x1 _y1`
* `curveto _x1, _y1, _x2, _y2, _x3, _y3`
* `curveto _x2, _y2, _x3, _y3, _se`
* `circle _x, _y, _r`
* `ellipse _x, _y, _a, _b`
* `spline _x1, _y1, _x2, _y2`
* `pie _x, _y, _a, _b, _alpha, _beta`

* `stroke`
* `fill`
* `fill _b`
* `fillstroke`
* `fillstroke _b`
* `clip`
* `clip _b`

### Text Commands

* `starttext`
* `endtext`
* `movetext _x, _y`
* `font _name, _size`
* `text _text0, ..., _textN`
* `rendertext _text`
* `rendertext _text, _align`
* `rendertext _text, _align, _width`
* `rendertext _text, _align, _width, _fn, _fs`
* `hscale _d`

