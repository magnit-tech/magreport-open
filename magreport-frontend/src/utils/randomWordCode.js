function getRandomInt(max) {
    return Math.floor(Math.random() * Math.floor(max));
}

export function randomWordCode(len){
    //let vowels = ['A', 'E', 'I', 'O', 'U', 'Y'];
    //let consonants = ['D','F','G'];

    let letters = [ ['A', 'E', 'I', 'O', 'U', 'Y'],
                    ['B', 'C', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Z']];

    let str = [];
    let firstLetterType = getRandomInt(2);

    for(let i = 0; i < len; i++){
        let letterType = (i % 2 + firstLetterType) % 2;

        let n = 6;
        if(letterType === 1){
            n = 20;
        }
        str.push(letters[letterType][getRandomInt(n)]);
    }

    return str.join('');
}

/* Еще один алгоритм формирования кодового слова с возможностью использования цифр и со слогами в 2 и 3 буквы

function getCode(syllableNum, useNums){

    // Наиболее подходящие согласные для использования их в качестве заглавных
    var cCommon = "bcdfghklmnprstvz"; 
    var cAll = cCommon + "jqwx";    // Все согласные 
    var vAll = "aeiouy";    // Все гласные 
    var lAll = cAll + vAll;    // Все буквы

    function rand(from, to) { 
        from = typeof(from) != 'undefined' ? from : 0;    // параметры
        to = typeof(to) != 'undefined' ? to : from + 1;    // по умолчанию
        return Math.round(from + Math.random()*(to - from)); 
    };

    function getRandChar(a) { 
        return a.charAt(rand(0,a.length-1)); 
    }

    // Коэффициент определяющий вероятность появления числа между слогами
    var numProb = 0, numProbStep = 0.25;
    let code = '';
    for(var i = 0; i < syllableNum; ++i) { 
        if(Math.round(Math.random())) { 
            code += getRandChar(cCommon) + 
                    getRandChar(vAll) + 
                    getRandChar(lAll); 
        } else { 
            code += getRandChar(vAll) + 
                    getRandChar(cCommon); 
        } 
        if(useNums && Math.round(Math.random() + numProb)) { 
            code += rand(0,9); 
            numProb += numProbStep; 
        } 
    } 
    return code.toUpperCase(); 
} 

*/