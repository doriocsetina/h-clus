public class AverageLinkDistance implements ClusterDistance {

    /**
     * 
     * @param c1
     * @param c2
     * @param d
     * @return
     */
    public double distance(Cluster c1, Cluster c2, Data d) {
        double sum = 0;

        for (int i = 0; i < c1.getSize(); i++) {
			Example e1 = d.getExample(c1.getElement(i));
			for (int j = 0; j < c2.getSize(); j++) {
				double distance = e1.distance(d.getExample(c2.getElement(j)));
				sum += distance;
			}
		}
        return sum/(c1.getSize()*c2.getSize());
    }
}
