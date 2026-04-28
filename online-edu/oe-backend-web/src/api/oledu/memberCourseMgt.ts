import { ContentTypeEnum } from '@/constants';
import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/oeMemberCourse/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/oeMemberCourse/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/oeMemberCourse/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/oeMemberCourse/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/oeMemberCourse/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/oeMemberCourse/delete',
    data,
  });
}

export function findByMemberId(memberId: number) {
  return request.get<any>({
    url: `/admin/oeMemberCourse/findByMemberId?memberId=${memberId}`,
  });
}

export function activateCourse(memberId: number, courseId: number) {
  return request.post<any>({
    url: '/admin/oeMemberCourse/activateCourse',
    headers: { 'Content-Type': ContentTypeEnum.FormURLEncoded },
    params: { memberId, courseId },
  });
}

export function batchSave(memberId: number, data: any) {
  return request.post<any>({
    url: `/admin/oeMemberCourse/batchSave/${memberId}`,
    data,
  });
}
