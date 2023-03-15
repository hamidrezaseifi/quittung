import os
import shutil

from google_drive_downloader import GoogleDriveDownloader as gdd
import json
#from pyunpack import Archive
import py7zr


# https://drive.google.com/file/d/1mpIpA_xOcNgCRtKrF55_kASJLiE8VvLh/view?usp=sharing

# https://drive.google.com/file/d/17ajbeepuuNDPcG56N2U67ENkbi2aNvew/view?usp=sharing


last_file_json_google_id = "17ajbeepuuNDPcG56N2U67ENkbi2aNvew"

last_file_json_data = {}

download_last_file_json_path = './last_file.json'

extract_temp_path = "./temp_download"


def download_last_file_json():
    gdd.download_file_from_google_drive(file_id=last_file_json_google_id,
                                        dest_path=download_last_file_json_path,
                                        unzip=False)


def download_archive():
    gdd.download_file_from_google_drive(file_id=last_file_google_id,
                                        dest_path=download_file_path,
                                        unzip=False)


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    download_last_file_json()

    with open(download_last_file_json_path) as json_file:
        last_file_json_data = json.load(json_file)
        print(last_file_json_data)
    json_file.close()

    os.remove(download_last_file_json_path)

    last_file_google_id = last_file_json_data["google_id"]
    last_file_name = last_file_json_data["file_name"]

    download_file_path = './' + last_file_name
    if os.path.exists(download_file_path):
        os.remove(download_file_path)

    download_archive()

    if os.path.exists(extract_temp_path):
        shutil.rmtree(extract_temp_path)
    os.mkdir(extract_temp_path)

    archive = py7zr.SevenZipFile(download_file_path, mode='r')
    archive.extractall(path=extract_temp_path)
    archive.close()

    if os.path.exists(download_file_path):
        os.remove(download_file_path)

    print('PyCharm')

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
