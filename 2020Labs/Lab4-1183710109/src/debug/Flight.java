package debug;

import java.util.Calendar;

public class Flight {

	private String flightNo; // º½°àºÅ
	private Calendar flightDate; // º½°àÈÕÆÚ£¬ÀýÈç2020-01-01
	private String departAirport; // º½°à³ö·¢»ú³¡
	private String arrivalAirport;// º½°à½µÂä»ú³¡
	private Calendar departTime;// º½°àÆð·ÉÊ±¼ä
	private Calendar arrivalTime;// º½°à½µÂäÊ±¼ä
	private Plane plane = null;// Ö´·ÉµÄ·É»ú

	public String getFlightNo() {
		return flightNo;
	}

	public void setFlightNo(String flightNo) {
		this.flightNo = flightNo;
	}

	public Calendar getFlightDate() {
		return flightDate;
	}

	public void setFlightDate(Calendar flightDate) {
		this.flightDate = flightDate;
	}

	public String getDepartAirport() {
		return departAirport;
	}

	public void setDepartAirport(String departAirport) {
		this.departAirport = departAirport;
	}

	public String getArrivalAirport() {
		return arrivalAirport;
	}

	public void setArrivalAirport(String arrivalAirport) {
		this.arrivalAirport = arrivalAirport;
	}

	public Calendar getDepartTime() {
		return departTime;
	}

	public void setDepartTime(Calendar departTime) {
		this.departTime = departTime;
	}

	public Calendar getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Calendar arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getPlaneNo() {
		return this.plane.getPlaneNo();
	}

	public String getPlaneType() {
		return this.plane.getPlaneType();
	}

	public int getSeatsNum() {
		return this.plane.getSeatsNum();
	}

	public double getPlaneAge() {
		return this.plane.getPlaneAge();
	}

	@Override
	public boolean equals(Object another) {
		if (another == null)
			return false;
		if (!(another instanceof Flight))
			return false;
		Flight anotherFlight = (Flight) another;
		if (anotherFlight.getFlightNo().equals(this.getFlightNo())
				&& anotherFlight.getFlightDate().equals(this.getFlightDate()))
			return true;
		return false;
	}

	@Override
	public int hashCode() {
		return this.flightNo.hashCode();
	}

	public Plane getPlane() {
		return plane;
	}

	public void setPlane(Plane plane) {
		this.plane = plane;
	}
}
