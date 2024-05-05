-- report
local math = require('math');
local string = require('string');

_pdf:set_title("TEST")
_pdf:set_subject("some")

_pdf:load_font("cr", "libertinus-mono-regular")
_pdf:load_font("hr", "pdf:helvetica")
_pdf:load_font("hb", "pdf:helvetica-bold")
_pdf:load_font("yr", "ysabeau-office-regular")
_pdf:load_font("yb", "ysabeau-office-black")

_pdf:new_page(595, 842)
_pdf:new_content()

_pdf:save()
_pdf:set_font( "hb", 30)
_pdf:draw_string("DISCLAIMER", 50, 700)
_pdf:draw_string("RIGHT", 500, 500, 'right')
_pdf:restore()


_pdf:end_content()

_pdf:end_page()
