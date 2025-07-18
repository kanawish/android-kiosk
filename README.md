# DiY Android Kiosks: Turning a Raspberry Pi into a Modular Workhorse

This repository contains backing source code for my Android Kiosks talk. You can find the [slides here](https://drive.google.com/file/d/1ILzekjc0K6_HWrF_qWhNrzkuwrKQz3ea/view?usp=sharing).

You might also be interested in my fork of https://github.com/kanawish/contrib-drivers with an example of locally publishing Android Things contributed drivers.


## Abstract 

What happens when you take a Raspberry Pi with a touchscreen, slap Android on it, and bolt it onto an 80/20 aluminum cube? You get a kiosk that does way more than your average locked-down tablet.

I'll show you how to build your own modular Android-based system that plays nice with USB peripherals you wouldn’t normally expect: Think webcams, and servos for automated 3D scanning and photography. We'll see how to stream or record video and audio, process it with ML models, and generate time-stamped work logs.

Add a barcode scanner, a payment terminal, and a scale to verify weights, and suddenly, you’ve got a DIY point-of-sale for pop-up stores, warehouses, or even recycling centers.

Join us for a fun exploration into how Android can be pushed beyond its usual limits, in kiosk mode!

## Working with a Raspi

shell:
`adb root`
`adb shell`
`#`
`alias l='ls -al --color'`
