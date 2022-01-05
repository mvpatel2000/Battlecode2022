from pathlib import Path
import shutil
import sys
import re
import os

if __name__ == '__main__':
    bot_dir = Path('./src/') / sys.argv[1]
    out_dir_name = 'smite'
    if len(sys.argv) > 2:
        out_dir_name = sys.argv[2]
    out_name = Path('./src/') / out_dir_name
    if out_name.exists():
        shutil.rmtree(out_name)
    zip_name = os.path.join('./src/', 'smite.zip')
    if os.path.exists(zip_name):
        os.remove(zip_name)
    out_name.mkdir()
    for f in bot_dir.iterdir():
        out = out_name / f.name
        Path.touch(out)
        with open(f, 'r') as file:
            content = file.read()
        out_content = content
        out_content = re.sub('rc.resign\(', '//rc.resign\(', out_content)
        out_content = re.sub('System.out.println\(', '//System.out.println\(', out_content)
        out_content = re.sub('System.out.print\(', '//System.out.print\(', out_content)
        out_content = re.sub('ec.rc.setIndicator', '//ec.rc.setIndicator', out_content)
        out_content = re.sub('rc.setIndicator', '//rc.setIndicator', out_content)
        out_content = re.sub('package [^\n]*;', 'package ' + out_dir_name + ';', out_content)
        with open(out, 'w') as out_file:
            out_file.write(out_content)
        os.system('zip -r ./src/' + out_dir_name + '.zip ./src/' + out_dir_name)