from re import match
def colorize(code, out):
    last_color = 'WHITE'
    buf = []
    for line in out.split("\n"):
        buf.append(line)
        if match("\[.*(OK|PASSED).*\]", line):
            buf, last_color = print_lines('GREEN', buf)
        elif match("\[.*FAILED.*\]", line):
            buf, last_color = print_lines('RED', buf)
        elif match("\[.*[-=]+.*\]", line):
            buf, last_color = print_lines('GREEN', buf)

    # Flush any pending items as green
    buf = print_lines('GREEN', buf)

def summary(bld):
    lst = getattr(bld, 'utest_results', [])
    for (f, code, out, err) in lst:
        colorize(code, out)

from waflib import Logs
def print_lines(color, lines):
    Logs.pprint(color, "\n".join(lines))
    return [], color

