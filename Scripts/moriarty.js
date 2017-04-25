var car = SpriteBinder.loadSprite("car.png");
var bird = SpriteBinder.loadSprite("bird.png");

function init(){

}

function tick(){

}

function render(g){
	car.render(32 * 12 * 4, 32 * 6 * 4, (car.width * 4), (car.height * 4), g);
	bird.render(32 * 12 * 4, 32 * 5 * 4, (bird.width * 4), (bird.height * 4), g);
}
