package wang.wangby.model.common;

import lombok.Data;

@Data
public class KeyValue<Key,Value> {

	private Key key;
	private Value value;

	public KeyValue(){}

	public KeyValue(Key key,Value value) {
		this.key=key;
		this.value=value;
	}
	
}
