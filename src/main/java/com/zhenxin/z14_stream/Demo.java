package com.zhenxin.z14_stream;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author xzhen
 * @created 17:41 01/04/2019
 * @description TODO
 */
public class Demo {
    public static void main(String[] args) {
        List<MemberChannelDTO> list = new ArrayList<>();
        MemberChannelDTO dto = new MemberChannelDTO();
        dto.setMemberChannelId("11111111111111");
        list.add(dto);
        MemberChannelDTO dto2 = new MemberChannelDTO();
        dto2.setMemberChannelId("2222222222222");
        list.add(dto2);
        MemberChannelDTO dto3 = new MemberChannelDTO();
        dto3.setMemberChannelId("3333333333333");
        list.add(dto3);

        list.stream().flatMap(item -> {
            if (item.getMemberChannelId().contains("2")) {
                MemberChannelDTO dto4 = new MemberChannelDTO();
                dto4.setMemberChannelId("444444444444444");
                return Stream.of(item, dto4);
            }
            return Stream.of(item);
        }).forEach(item -> {
            System.out.println(item.getMemberChannelId());
        });

        Stream.of(1, 2, 3, 4).sorted(Comparator.comparingInt(i -> i)).forEach(System.out::println);

    }

    static class MemberChannelDTO {
        private String memberChannelId;

        public String getMemberChannelId() {
            return memberChannelId;
        }

        public void setMemberChannelId(String memberChannelId) {
            this.memberChannelId = memberChannelId;
        }
    }
}
