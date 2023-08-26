## Types:

```ts
// string
const brand: string = 'chevrolet';
// with inference - possible when we declare it at definition
const brand = 'chevrolet';

// number
const age: number = 7;
const height = 5.6;

// Custom Types
type Animal = 'Cheetah' | 'Lion'
const animal: Animal = 'Lion'
const turtle: Animal = 'Turtle' // Compilation Error - not assignable

// Enums
enum Brands { Chevrolet, Cadillac, Ford }
const myCar: Brands = Brands.Cadillac

// Enums with Values
enum Brands { Tesla = 1, GMC, Jeep }
const myTruck: BrandsReduced = BrandsReduced.GMC
```

## Decorators:
  - add metadata to declarations for further use
  - can impact how classes, methods, functions, properties behave
    - by simply altering the data we define in fields or params
  - 4 Types of Decorators
    - Class Decorators
    - Property
    - Method
    - Parameter

### Class Decorator:
  - allows:
    - augmentation of a class
    - perform operations on its members
    - decorators are executed before the class gets instantiated
  - creating a class decorator requires defining a plain function
    - whose signature is a pointer to the constructor belonging to the class we want to decorate

Formal Declaration of a class decorator:
```ts
declare type ClassDecorator <TFunction extends Function>(Target: TFunction) => TFunction | void
```

Sample:
```ts
// Decorator
// notice that we are decorating a `target: Function`, this Function is that will be decorated is the Constructor
function Banana(target: Function): void {
  target.prototype.banana = function(): void {
    console.log('We have bananas!');
  }
}

@Banana
class FruitBasket { }

const basket = new FruitBasket();
basket.banana();
```

Extending a Class Decorator
```ts
function Banana(message: string) {
  return function(target: Function) {
    target.prototype.banana = function(): void {
      console.log(message);
    }
  }
}

// arrow syntax
function Banana(message: string) {
  return (target: Function) => {
    target.prototype.banana = function(): void {
      console.log(message)
    }
  }
}

// using the extended decorator
@Banana('Bananas are yellow')
class FruitBasket { }

const basket = new FruitBasket();
basket.banana();
```

### Property Decorators
  - defined by creating a fx whose signature takes 2 params:
    - `target` - prototype of the class we want to decorate
    - `key` - name of the property we want to decorate

Sample:
```ts
function Jedi(target: Object, key: string) {
  let propertyValue: string = target[key];
  // delete operator deletes the property from object
  // returns true if successful
  if (delete target[key]) {
    Object.defineProperty(target, key, {
      get: function() {
        return propertyValue;
      },
      set: function(newValue) {
        propertyValue = newValue;
        console.log(`${propertyValue} is a Jedi`)
      }
    })
  }
}

class Character {
  @Jedi
  name: string;
}

const character = new Character();
character.name = 'Luke';
```