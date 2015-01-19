# Defaults
Framer.Defaults.Animation =
    curve: 'spring(150, 15, 0)'
    
# Sound
soundLayer = new Layer()
soundLayer.opacity = 0
soundLayer.html = '<audio src="sounds/coin.mp3" preload="auto"></audio>'
soundPlayer = soundLayer.querySelector 'audio'

# Background
background = new Layer x: 0, y: 0, width: 1080, height: 1920, image: 'images/ZeroDay.png'

# ZERODAY text
zeroday = new Layer x: 0, y: 0, width: 976, height: 165, image: 'images/ZERODAY text.png'
#zeroday.opacity = 0

# Coin jump up anim
coolLine = new Layer x: background.width/2, y: background.height/2 + 120, width: 0, height: 24, image: 'images/line.png'

coinVisibleSuperLayer = new Layer()
coinVisibleSuperLayer.backgroundColor = 'transparent'
coinVisibleSuperLayer.width = 400
coinVisibleSuperLayer.height = 800
coinVisibleSuperLayer.center()
coinVisibleSuperLayer.y = 292

bigCoin = new Layer x: 41, y: 800, width: 318, height: 318, image: 'images/Coin 2.png', superLayer: coinVisibleSuperLayer

# Gradient
gradient = new Layer x: 0, y: 0, width: 1080, height: 1776, image: 'images/gradient.png'
gradient.opacity = 0

plusOne = new Layer x: 572, y: 130, width: 45, height: 35, image: 'images/ 269.png'
plusOne.opacity = 0

zeroday.center()
zeroday.draggable.enabled = true

zeroday.on Events.DragStart, ->
	gradient.animate 
		properties:
			opacity: 0.8
		delay: 0.1
		
	zeroday.animate
		properties:
			scale: 0.9
	 
zeroday.on Events.DragEnd, -> 
	gradient.animate
		properties:
			opacity: 0
			
	if zeroday.y > 1400
		zeroday.animate
			properties:
				y: background.height
		
		zeroday.animations()[0].on Events.AnimationEnd, ->
			soundPlayer.play()
			
			coolLine.animate
				properties:
					width: 490
					x: background.width/2 - 245
					
			coolLine.animations()[0].on Events.AnimationEnd, ->
				bigCoin.animate			
					properties:
						y: 300
				
			plusOne.animate
				properties:
					opacity: 1
					y: 90
					
			plusOne.animations()[0].on Events.AnimationEnd, ->
				plusOne.animate
					properties: 										opacity: 0	
	else
		zeroday.animate
			properties:
				scale: 1.0
				x: background.width / 2 - zeroday.width / 2
				y: background.height / 2 - zeroday.height / 2

navBar = new Layer x: 0, y: background.height - 144, width: 1080, height: 144, image: 'images/NavBar.png'
statusBar = new Layer x: 0, y: 0, width: 1080, height: 71, image: 'images/StatusBar.png'








