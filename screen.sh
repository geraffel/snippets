#!/bin/sh

screen -dmS mysess -t ASDF
screen -S mysess -X screen -t QWER htop

screen -R
