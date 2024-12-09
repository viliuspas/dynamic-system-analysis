import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

class Painter extends JPanel {
    final Function function = Constants.function;
    final int fHeight = Constants.FRAME_HEIGHT;
    final int fWidth = Constants.FRAME_WIDTH;
    //final int functionMoveInterval = Constants.FUNCTION_MOVE_INTERVAL;
    private double domainMin = Constants.DOMAIN_MIN;
    private double domainMax = Constants.DOMAIN_MAX;
    private double domainMaxY = Constants.DOMAIN_MAX;
    private double domainMinY = Constants.DOMAIN_MIN;
    final double dotDensity = Constants.DOT_DENSITY;
    final int zoomInterval = Constants.ZOOM_INTERVAL;
    private int zoom = Constants.DEFAULT_ZOOM_MULTIPLIER;
    final int maxJump = Constants.MAX_JUMP;
    final double unitLineSize = Constants.UNIT_LINE_SIZE;
    final int orbitStepCount = Constants.ORBIT_STEP_COUNT;
    final int aRange = Constants.ARange;
    private String inputText = "1";
    private String orbitPoint = "1";
    private boolean drawOrbitState = false;
    private boolean drawFeigenbaumState = false;
    private boolean drawAtoNState = false;
    private boolean drawExtraStats = false;
    private int functionOffsetX = 0;
    private int functionOffsetY = 0;
    private int domainOffsetX = 0;
    private int domainOffsetY = 0;
    //private int domainRange = 0;
    private Map<Double, Set<Double>> points = new TreeMap<>();
    private boolean isFeigenbaumGenerated = false;
    public JLabel intervalLabel;

    public Painter(JLabel intervalLabel) {
        super();

        this.intervalLabel = intervalLabel;
    }


    public void setDrawOrbitState(boolean state) {
        drawOrbitState = state;
    }

    public boolean getDrawOrbitState() {
        return drawOrbitState;
    }

    public void setDrawFeigenbaumState(boolean state) {
        drawFeigenbaumState = state;
    }
    public boolean getDrawFeigenbaumState() {
        return drawFeigenbaumState;
    }

    public void setDrawAToNState(boolean state){ drawAtoNState = state; }
    public boolean getDrawAToAnState() { return drawAtoNState; }

    public void setDrawExtraStats(boolean state){ this.drawExtraStats = state; }
    public boolean getDrawExtraStats(){ return drawExtraStats; }

//    public void moveLeft() {
//        domainRange -= 1;
//    }
//
//    public void moveRight() {
//        domainRange += 1;
//    }

    public int getFunctionOffsetX() {
        return functionOffsetX;
    }

    public void moveFunction(int x, int y) {
        functionOffsetX += x;
        functionOffsetY -= y;
        domainOffsetX = (functionOffsetX/zoom);
        domainOffsetY = (functionOffsetY/zoom);
    }

    public void resetToStart() {
        functionOffsetX = 0;
        functionOffsetY = 0;
        domainOffsetX = 0;
        domainOffsetY = 0;
    }

    public void increaseZoom() {
        zoom += zoomInterval;
    }

    public void decreaseZoom() {
        if (zoom > 10) {
            if(zoom - zoomInterval < 10) {
                zoom = 10;
            }
            else {
                zoom -= zoomInterval;
            }
        }
    }

    public int getZoom() {
        return zoom;
    }

    public void setInputText(String inputText){
        this.inputText = inputText;
    }

    public String getInputText(){
        return inputText;
    }

    public void setOrbitPoint(String orbitPoint){
        this.orbitPoint = orbitPoint;
    }

    public String getOrbitPoint(){
        return orbitPoint;
    }

    public Color getRandomColor(){
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        return new Color(r, g, b);
    }

    public void drawCoordinateSystem(Graphics g) {
        // x-axis
        g.drawLine(0, convertY(0) + functionOffsetY, fWidth, convertY(0) + functionOffsetY);
        // y-axis
        g.drawLine(convertX(0) - functionOffsetX, 0, convertX(0) - functionOffsetX, fHeight);

        int y1 = convertY(-unitLineSize) + functionOffsetY;
        int y2 = convertY(unitLineSize) + functionOffsetY;
        int x1 = convertX(unitLineSize) - functionOffsetX;
        int x2 = convertX(-unitLineSize) - functionOffsetX;

        // x-axis unit lines
        for (double i = domainMin + domainOffsetX; i < domainMax + domainOffsetX; i += 0.5) {
            int x = convertX(i) - functionOffsetX;
            char[] value = Double.toString(i).toCharArray();

            g.drawLine(x, y1, x, y2);

            if(i == (int)i && getZoom() >= 30 && i != 0) {
                g.drawChars(value, 0, value.length - 2, x - 3, y1 + 15);
            }
        }
        // y-axis unit lines
        for (double i = domainMinY + domainOffsetY; i < domainMaxY + domainOffsetY; i += 0.5) {
            int y = convertY(i) + functionOffsetY;
            char[] value = Double.toString(i).toCharArray();

            g.drawLine(x1, y, x2, y);

            if(i == (int)i && getZoom() >= 30 && i != 0) {
                g.drawChars(value, 0, value.length - 2, x1 + 5, y + 4);
            }
        }
    }

    public int convertX(double x) {
        return (int)(fWidth/2 + x * zoom);
    }

    public int convertY(double y) {
        return (int)(fHeight/2 - y * zoom);
    }

    public void drawOrbit(Graphics g, double a, Function function) {

        boolean isDefined = true;
        double selectedX = 1;
        double selectedY = function.execute(selectedX, a);
        double coefficient = selectedY / selectedX;

        drawFunction(g, a, Color.red, (x, param) -> coefficient * x);

        double startX = Double.parseDouble(getOrbitPoint());

        g.setColor(Color.green);

        double currentX = startX;
        double currentY = 0;
        for(int i = 0; i < orbitStepCount ; i++) {
            double function1 = function.execute(currentX, a);

            g.drawLine(convertX(currentX) - functionOffsetX,
                    convertY(currentY) + functionOffsetY,
                    convertX(currentX) - functionOffsetX,
                    convertY(function1) + functionOffsetY);

            double functionX = function1 / coefficient;

            g.drawLine(convertX(currentX) - functionOffsetX,
                    convertY(function1) + functionOffsetY,
                    convertX(functionX) - functionOffsetX,
                    convertY(function1) + functionOffsetY);

            currentX = functionX;
            currentY = function1;
        }
    }

    public void drawFunction(Graphics g, double a, Color color, Function function) {
        Integer lastX = null;
        Integer lastY = null;

        g.setColor(color);

        for (double i = domainMin + domainOffsetX; i < domainMax + domainOffsetX; i += dotDensity) {
            double functionY = function.execute(i, a);

            int x = convertX(i);
            int y = convertY(functionY);

            if(lastX == null){
                lastX = x;
                lastY = y;
            }

            // ensures that the distance between points isn't too big
            // avoids calculation / drawing errors
            if(Math.abs(x - lastX) < maxJump && Math.abs(y - lastY) < maxJump){
                g.drawLine(lastX - functionOffsetX, lastY + functionOffsetY, x - functionOffsetX, y + functionOffsetY);
            }

            lastX = x;
            lastY = y;
        }
    }

    public void drawLogisticsMap(Graphics g, Color color, Function function, int maxBifurcationCount){
        if(!isFeigenbaumGenerated){
            generateLogisticsMapPoints(0,8,1000, 10000, 300, function);
        }

        int branchCount = (int)Math.pow(2, maxBifurcationCount);

        int bifurcationCount = 1;
        for (Double a : points.keySet()){
            int x = convertX(a);
            for (Double xn : points.get(a)){
                int y = convertY(xn);
                g.setColor(color);
                g.drawOval(x - functionOffsetX - 1, y + functionOffsetY - 1, 1, 1);


                    if (bifurcationCount < points.get(a).size() && bifurcationCount < branchCount){
                        bifurcationCount = points.get(a).size();
                        if (getDrawExtraStats()){
                            g.setColor(Color.BLUE);
                            g.drawLine(x - functionOffsetX, 0, x - functionOffsetX, fHeight);
                            g.setColor(Color.BLACK);
                            String value = "a = "+ String.format("%.3f", a);
                            g.drawString(value, x - functionOffsetX - 50, y + functionOffsetY - 30);
                            g.drawLine(x - functionOffsetX - 30, y + functionOffsetY - 30, x - functionOffsetX, y + functionOffsetY);
                        }
                    }

                // feigenbaum bifurcation ending line
                if (bifurcationCount > 1 && points.get(a).size() == 1){
                    if (getDrawExtraStats()){
                        g.setColor(Color.cyan);
                        g.drawLine(x - functionOffsetX, 0, x - functionOffsetX, fHeight);
                        g.drawLine(-functionOffsetX, 0, -functionOffsetX, fHeight);

                        g.setColor(Color.BLACK);
                        String value = "a = "+ String.format("%.3f", a);
                        g.drawString(value, x - functionOffsetX + 50, y + functionOffsetY - 30);
                        g.drawLine(x - functionOffsetX + 30, y + functionOffsetY - 30, x - functionOffsetX, y + functionOffsetY);
                    }
                    return;
                }
            }
        }
    }

    public void generateLogisticsMapPoints(double aMin, double aMax, int xDensity, int iterations, int yDensity, Function function) {

        // don't know where to start from
        double x0 = 0.5;
        double aStepCount = (aMax - aMin) / xDensity;

        for (int i = 0; i <= xDensity; i++) {
            double a = aMin + i * aStepCount;
            double x = x0;

            // Stabilize the system by iterating a number of times
            for (int j = 0; j < iterations; j++) {
                x = function.execute(x, a);
            }

            Set<Double> uniqueValues = new HashSet<>();
            for (int j = 0; j < yDensity; j++) {
                x = function.execute(x, a);
                uniqueValues.add(Math.round(x * 1000.0) / 1000.0);
            }
            points.put(a, uniqueValues);
        }
        isFeigenbaumGenerated = true;
    }

    public void drawAToN(Graphics g, Function function, Color color, double a){
        Double lastX = null;
        Double lastN = null;

        g.setColor(color);

        // don't know where to start from
        double x = Double.parseDouble(getOrbitPoint());

        for (double n = 0; n <= domainMax + domainOffsetX; n++) {
            if(lastN == null){
                lastN = n;
                lastX = x;
            }

            x = function.execute(x, a);

            if (n != 0){
                g.setColor(color);
                g.drawLine(convertX(lastN) - functionOffsetX, convertY(lastX) + functionOffsetY, convertX(n) - functionOffsetX, convertY(x) + functionOffsetY);
            }

            int dotSize = 8;
            g.setColor(Color.BLACK);
            g.fillOval(convertX(n) - functionOffsetX - dotSize / 2, convertY(x) + functionOffsetY - dotSize / 2, dotSize, dotSize);

            String stringValue = String.format("(%.2f; %.2f)", n, x);
            char[] value = stringValue.toCharArray();
            if(n == (int)n && getZoom() >= 50) {
                g.drawChars(value, 0, value.length, convertX(n) - functionOffsetX - 30, convertY(x) + functionOffsetY - 5);
            }

            lastN = n;
            lastX = x;
        }
    }

    public boolean isDefined(double a) {

        double selectedX = 1;
        double selectedY = selectedX * Math.exp(a * (1 - selectedX));
        double coefficient = selectedY / selectedX;

        double currentX = Double.parseDouble(getOrbitPoint());
        for(int i = 0; i < orbitStepCount ; i++) {
            double function1 = function.execute(currentX, a);

            if(Double.isInfinite(function1)) {
                return false;
            }

            double functionX = function1 / coefficient;

            currentX = functionX;
        }

        return true;
    }

    class Range {
        double start;
        double end;
        boolean isDefined;
        boolean startInclusive;
        boolean endInclusive;

        @Override
        public String toString() {
            String rangeString = "";
            String startString = getNumberString(start);
            String endString = getNumberString(end);

            if(startInclusive) {
                rangeString += "[";
            }
            else {
                rangeString += "(";
            }

            rangeString += startString + " ; " + endString;

            if(endInclusive) {
                rangeString += "]";
            }
            else {
                rangeString += ")";
            }

            return rangeString;
        }

        public String getNumberString(Double number) {
            if(Double.isInfinite(number)) {
                if(number < 0) {
                    return "-inf";
                }
                else {
                    return "+inf";
                }
            }
            else {
                return Double.toString(Math.round(number));
            }
        }

        public Range(double start, double end, boolean isDefined, boolean startInclusive, boolean endInclusive) {
            this.start = start;
            this.end = end;
            this.isDefined = isDefined;
            this.startInclusive = startInclusive;
            this.endInclusive = endInclusive;
        }
    }

    public void calculateARange(Graphics g, Function function) {

        double lastA = Double.NEGATIVE_INFINITY;
        boolean isCurrentDefined = isDefined(-aRange);
        boolean isLastDefined;
        List<Range> ranges = new ArrayList<>() {
            @Override
            public String toString() {
                String rangesString = "";

                for(Range range : this) {
                    rangesString += (range.isDefined ? "DEFINED: " : "UNDEFINED: ");
                    rangesString += range + "   ";
                }

                return rangesString;
            }
        };

        isLastDefined = isCurrentDefined;
        for(double i = -aRange + 0.1; i <= aRange; i += 0.1){
            isCurrentDefined = isDefined(i);


            if(isLastDefined != isCurrentDefined) {
                ranges.add(new Range(lastA, i, isLastDefined, false, isCurrentDefined));
                lastA = i;
            }
            isLastDefined = isCurrentDefined;
        }

        ranges.add(new Range(lastA, Double.POSITIVE_INFINITY, isLastDefined, false, false));


        intervalLabel.setText(ranges.toString());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawCoordinateSystem(g);

        if (!getDrawFeigenbaumState()) {
            double a = Double.parseDouble(getInputText());

            if (getDrawAToAnState()) {
                // a to n
                drawAToN(g,function,Color.RED, a);
            }
            else {
                drawFunction(g, a, Color.blue, function);

                //orbit
                if(getDrawOrbitState()){
                    drawOrbit(g, a, function);
                    calculateARange(g,function);
                }
            }
        }
        else {
            drawLogisticsMap(g, Color.RED, function, 3);
        }
    }
}
