/**
 * Copyright 2010 Jason L. Hudgins
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.droidworks.parsers.twitter;

import java.util.ArrayList;

/**
 * @author jasonhudgins
 *
 */
public class TwitterFeed {

	private String mName;
	private String mScreenName;
	private String mLocation;
	private String mDescription;
	private String mImageUrl;
	private int mFollowerCount;
	private int mFriendCount;

	private final ArrayList<Tweet> mTweets = new ArrayList<Tweet>();

	public void addTweet(Tweet tweet) {
		mTweets.add(tweet);
	}

	public Tweet getTweet(int position) {
		return mTweets.get(position);
	}

	public int getCount() {
		return mTweets.size();
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getScreenName() {
		return mScreenName;
	}

	public void setScreenName(String screenName) {
		mScreenName = screenName;
	}

	public String getLocation() {
		return mLocation;
	}

	public void setLocation(String location) {
		mLocation = location;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		mDescription = description;
	}

	public String getImageUrl() {
		return mImageUrl;
	}

	public void setImageUrl(String imageUrl) {
		mImageUrl = imageUrl;
	}

	public int getFollowerCount() {
		return mFollowerCount;
	}

	public void setFollowerCount(int followerCount) {
		mFollowerCount = followerCount;
	}

	public int getFriendCount() {
		return mFriendCount;
	}

	public void setFriendCount(int friendCount) {
		mFriendCount = friendCount;
	}

}
