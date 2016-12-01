package com.fogok.explt.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

public class AtlasLoader {

	private Texture atlasTexture;

    private ArrayList<Integer> positionsX = new ArrayList<Integer>(120);
    private ArrayList<Integer> positionsY = new ArrayList<Integer>(120);
    private ArrayList<Integer> sizesWidth = new ArrayList<Integer>(120);
    private ArrayList<Integer> sizesHeight = new ArrayList<Integer>(120);


    //namesTextureRegions

    public static final int OBJ_MAIN_CUBE = 0;

    ///


	public AtlasLoader(int whoAtlas) {
        String file;
        float cffSize;


        file = "atls" + whoAtlas + ".png";

        cffSize = 1f;

        atlasTexture = new Texture(file);
		atlasTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		ArrayList<Integer> numb = new ArrayList<Integer>();

		String str, str2 = "";

		Integer hq[], jd = 0;

		hq = new Integer[6];

        str = Gdx.files.internal("atls" + whoAtlas + ".txt").readString();

		for (char element : str.toCharArray()) {
			if (element != ' ') {
				str2 = str2 + String.valueOf(element);
			} else {
				numb.add(Integer.parseInt(str2));
				str2 = "";
			}
		}

		for (int i = 0; i < numb.size() - 1; i++) {
			hq[jd] = numb.get(i);
			jd++;
			if (jd == 4) {
				jd = 0;



				positionsX.add((int) (hq[0] * cffSize));
				positionsY.add((int) (hq[1] * cffSize));
				sizesWidth.add((int) (hq[2] * cffSize));
				sizesHeight.add((int) (hq[3] * cffSize));

			}

		}
		hq[3] = numb.get(numb.size() - 1);

        positionsX.add((int) (hq[0] * cffSize));
        positionsY.add((int) (hq[1] * cffSize));
        sizesWidth.add((int) (hq[2] * cffSize));
        sizesHeight.add((int) (hq[3] * cffSize));

        numb = null;
	}
	
	public void dispose(){
        positionsX = null;
        positionsY = null;
        sizesWidth = null;
        sizesHeight = null;
		atlasTexture.dispose();
	}

	public TextureRegion getTG(int wg) {
		return new TextureRegion(atlasTexture, positionsX.get(wg), positionsY.get(wg), sizesWidth.get(wg),
				sizesHeight.get(wg));
	}
}
