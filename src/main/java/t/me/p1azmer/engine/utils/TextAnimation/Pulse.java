package t.me.p1azmer.engine.utils.TextAnimation;

import java.util.ArrayList;
import java.util.List;

public class Pulse {

    public static List<String> execute(String text, String color, int pause) {
        ArrayList<String> frames = new ArrayList<>();
        String fadeto = color.toLowerCase();
        byte var7 = -1;
        switch (fadeto.hashCode()) {
            case -734239628:
                if (fadeto.equals("yellow")) {
                    var7 = 4;
                }
                break;
            case 112785:
                if (fadeto.equals("red")) {
                    var7 = 3;
                }
                break;
            case 3027034:
                if (fadeto.equals("blue")) {
                    var7 = 5;
                }
                break;
            case 3068707:
                if (fadeto.equals("cyan")) {
                    var7 = 8;
                }
                break;
            case 3441014:
                if (fadeto.equals("pink")) {
                    var7 = 6;
                }
                break;
            case 93818879:
                if (fadeto.equals("black")) {
                    var7 = 2;
                }
                break;
            case 98619139:
                if (fadeto.equals("green")) {
                    var7 = 7;
                }
                break;
            case 104256825:
                if (fadeto.equals("multi")) {
                    var7 = 0;
                }
                break;
            case 113101865:
                if (fadeto.equals("white")) {
                    var7 = 1;
                }
        }

        int i;
        label233:
        {
            label234:
            {
                label235:
                {
                    label236:
                    {
                        label237:
                        {
                            switch (var7) {
                                case 0:
                                case 1:
                                    frames.add("§0" + text);
                                    frames.add("§8" + text);
                                    frames.add("§7" + text);
                                    frames.add("§f" + text);

                                    for (i = 0; i < pause; ++i) {
                                        frames.add("§f" + text);
                                    }

                                    frames.add("§f" + text);
                                    frames.add("§7" + text);
                                    frames.add("§8" + text);
                                    frames.add("§0" + text);

                                    for (i = 0; i < pause; ++i) {
                                        frames.add("§0" + text);
                                    }

                                    if (!fadeto.equalsIgnoreCase("multi")) {
                                        return frames;
                                    }
                                case 2:
                                    frames.add("§f" + text);
                                    frames.add("§7" + text);
                                    frames.add("§8" + text);
                                    frames.add("§0" + text);

                                    for (i = 0; i < pause; ++i) {
                                        frames.add("§0" + text);
                                    }

                                    frames.add("§0" + text);
                                    frames.add("§7" + text);
                                    frames.add("§8" + text);
                                    frames.add("§0" + text);

                                    for (i = 0; i < pause; ++i) {
                                        frames.add("§0" + text);
                                    }

                                    if (!fadeto.equalsIgnoreCase("multi")) {
                                        return frames;
                                    }
                                case 3:
                                    break;
                                case 4:
                                    break label237;
                                case 5:
                                    break label236;
                                case 6:
                                    break label235;
                                case 7:
                                    break label234;
                                case 8:
                                    break label233;
                                default:
                                    return frames;
                            }

                            frames.add("§0" + text);
                            frames.add("§8" + text);
                            frames.add("§4" + text);
                            frames.add("§c" + text);

                            for (i = 0; i < pause; ++i) {
                                frames.add("§c" + text);
                            }

                            frames.add("§c" + text);
                            frames.add("§4" + text);
                            frames.add("§8" + text);
                            frames.add("§0" + text);

                            for (i = 0; i < pause; ++i) {
                                frames.add("§0" + text);
                            }

                            if (!fadeto.equalsIgnoreCase("multi")) {
                                return frames;
                            }
                        }

                        frames.add("§0" + text);
                        frames.add("§8" + text);
                        frames.add("§6" + text);
                        frames.add("§e" + text);

                        for (i = 0; i < pause; ++i) {
                            frames.add("§e" + text);
                        }

                        frames.add("§e" + text);
                        frames.add("§6" + text);
                        frames.add("§8" + text);
                        frames.add("§0" + text);

                        for (i = 0; i < pause; ++i) {
                            frames.add("§0" + text);
                        }

                        if (!fadeto.equalsIgnoreCase("multi")) {
                            return frames;
                        }
                    }

                    frames.add("§0" + text);
                    frames.add("§8" + text);
                    frames.add("§1" + text);
                    frames.add("§9" + text);

                    for (i = 0; i < pause; ++i) {
                        frames.add("§9" + text);
                    }

                    frames.add("§9" + text);
                    frames.add("§1" + text);
                    frames.add("§8" + text);
                    frames.add("§0" + text);

                    for (i = 0; i < pause; ++i) {
                        frames.add("§0" + text);
                    }

                    if (!fadeto.equalsIgnoreCase("multi")) {
                        return frames;
                    }
                }

                frames.add("§0" + text);
                frames.add("§8" + text);
                frames.add("§5" + text);
                frames.add("§d" + text);

                for (i = 0; i < pause; ++i) {
                    frames.add("§d" + text);
                }

                frames.add("§5" + text);
                frames.add("§d" + text);
                frames.add("§8" + text);
                frames.add("§0" + text);

                for (i = 0; i < pause; ++i) {
                    frames.add("§0" + text);
                }

                if (!fadeto.equalsIgnoreCase("multi")) {
                    return frames;
                }
            }

            frames.add("§0" + text);
            frames.add("§8" + text);
            frames.add("§2" + text);
            frames.add("§a" + text);

            for (i = 0; i < pause; ++i) {
                frames.add("§a" + text);
            }

            frames.add("§a" + text);
            frames.add("§2" + text);
            frames.add("§8" + text);
            frames.add("§0" + text);

            for (i = 0; i < pause; ++i) {
                frames.add("§0" + text);
            }

            if (!fadeto.equalsIgnoreCase("multi")) {
                return frames;
            }
        }

        frames.add("§0" + text);
        frames.add("§8" + text);
        frames.add("§3" + text);
        frames.add("§b" + text);

        for (i = 0; i < pause; ++i) {
            frames.add("§b" + text);
        }

        frames.add("§b" + text);
        frames.add("§3" + text);
        frames.add("§8" + text);
        frames.add("§0" + text);

        for (i = 0; i < pause; ++i) {
            frames.add("§0" + text);
        }

        return frames;
    }
}