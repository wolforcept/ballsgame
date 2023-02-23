package wolforce.ballsgame;

import java.util.function.Consumer;

public enum SomeEnum {

	FOO(new Consumer<SomeClass>() {
		@Override
		public void accept(SomeClass someClass) {
			someClass.someEnum = SomeEnum.FOO; // NO ERROR HERE
		}
	}),

	// BAR(someClass -> someClass.someEnum = SomeEnum.BAR), // ERROR HERE

	BAR2(SomeEnum::methodA);
	;

	SomeEnum(Consumer<SomeClass> consumer) {
	}
	
	SomeEnum(int consumer) {
	}
	
	public static void methodA(SomeClass someClass) {
		
	}
}
