package main.java.gama.genstar.plugin.utils;

import core.metamodel.io.GSSurveyType;
import core.metamodel.value.IValue;
import core.metamodel.value.binary.BooleanValue;
import core.metamodel.value.numeric.ContinuousValue;
import core.metamodel.value.numeric.IntegerValue;
import core.metamodel.value.numeric.RangeValue;
import core.util.data.GSEnumDataType;
import main.java.gama.genstar.plugin.type.GamaRange;
import main.java.gama.genstar.plugin.type.GamaRangeType;
import msi.gama.runtime.IScope;
import msi.gaml.types.IType;

public class GenStarGamaUtils {
	public static GSSurveyType toSurveyType(String type) {
		if (type.equals("ContingencyTable"))
			return GSSurveyType.ContingencyTable;
		if (type.equals("GlobalFrequencyTable"))
			return GSSurveyType.GlobalFrequencyTable;
		if (type.equals("LocalFrequencyTable"))
			return GSSurveyType.LocalFrequencyTable;
		return GSSurveyType.Sample;
	}
	
	@SuppressWarnings("rawtypes")
	public static GSEnumDataType toDataType(final IType type, final boolean ordered) {
		int t = type.id();
		if (t == IType.FLOAT)
			return GSEnumDataType.Continue;
		if (t == IType.INT)
			return GSEnumDataType.Integer;
		if (t == IType.BOOL)
			return GSEnumDataType.Boolean;
		if (t == GamaRangeType.id  )
			return GSEnumDataType.Range;
		if (ordered)
			return GSEnumDataType.Order;
		return GSEnumDataType.Nominal; 
	}

	static public Object toGAMAValue(IScope scope, IValue val, boolean checkEmpty) {
		GSEnumDataType type= val.getType();
		if (checkEmpty && val.equals(val.getValueSpace().getEmptyValue())) return toGAMAValue(scope, val.getValueSpace().getEmptyValue(), false);
		if (type == GSEnumDataType.Boolean) {
			return ((BooleanValue) val).getActualValue();
		}
		if (type == GSEnumDataType.Continue) {
			if (val instanceof RangeValue) return toGAMARange(val);
			return ((ContinuousValue) val).getActualValue ();
		}
		if (type == GSEnumDataType.Integer) {
			if (val instanceof RangeValue) return toGAMARange(val);
			return ((IntegerValue) val).getActualValue();
		}
		if (type == GSEnumDataType.Range) {
			return toGAMARange(val);
		}
		return val.getStringValue();
	}
	
	static GamaRange toGAMARange(IValue val) {
		RangeValue rVal = (RangeValue) val;
		return new GamaRange(rVal.getBottomBound().doubleValue(), rVal.getTopBound().doubleValue());
	}
	
	// TODO Ben : à remettre si le précédent ne marche pas :-)
//	static GamaRange toGAMARange(IValue val) {
//		
//		Number[] vals = ((RangeValue) val).getActualValue();
//		if (vals.length == 0) return null;
//		Number rangeMin = vals[0];
//		Number rangeMax = vals.length > 1 ? vals[1] : Double.MAX_VALUE;
//		return new GamaRange(rangeMin.doubleValue(), rangeMax.doubleValue());
//	}

	@SuppressWarnings("rawtypes")
	public static Object toGAMAValue(IScope scope, IValue valueForAttribute, boolean checkEmpty, IType type) {
		Object gamaValue = toGAMAValue(scope, valueForAttribute, checkEmpty);
		if(type != null && gamaValue instanceof GamaRange) {
			return ((GamaRange) gamaValue).cast(scope, type);
		}
		return gamaValue;
	}
	
}