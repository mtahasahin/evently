import React, { useEffect } from 'react';
import { useRouter } from 'next/router';
import EventApi from '../../../../api/event.api';
import textAreaStyles from '../../../elements/TextArea/TextArea.module.css';
import textInputStyles from '../../../elements/TextInput/TextInput.module.css';
import EventApplication from '../../../modules/EventApplication';
import LoadingIndicator from '../../../elements/LoadingIndicator/LoadingIndicator';

const Question = ({ title, description, isRequired, type, answer }) => {
  return (
    <div className="w-full relative flex flex-col space-y-2 p-5 rounded border border-opacity-50 shadow-sm">
      <div className="flex flex-row justify-between">
        <div className="font-semibold">{title}</div>
        <div className="text-sm text-gray-400">
          {isRequired ? 'Required' : 'Optional'}
        </div>
      </div>
      <div className="text-sm">{description}</div>
      <div className="pt-3">
        {type === 'SHORT_TEXT' && (
          <div className="w-full">
            <input
              type="text"
              className={textInputStyles.base}
              readOnly
              defaultValue={answer || ''}
            />
          </div>
        )}

        {type === 'LONG_TEXT' && (
          <div className="w-full">
            <textarea
              className={textAreaStyles.base}
              readOnly
              defaultValue={answer || ''}
            />
          </div>
        )}
      </div>
    </div>
  );
};

const DisplayAnswersPage = () => {
  const { query } = useRouter();
  const { slug, applicationId } = query;

  const [eventApplication, setEventApplication] = React.useState(null);

  const setApproved = () => {
    setEventApplication((application) => ({
      ...application,
      has_approved: true,
    }));
  };

  useEffect(() => {
    if (slug && applicationId) {
      EventApi.getAnswer({
        slug,
        applicationId,
      }).then((res) => {
        const application = res.data.data;
        application.answers.forEach((answer) => {
          application.questions.find(
            (question) => question.id === answer.questionId
          ).answer = answer.answer;
        });
        setEventApplication(application);
      });
    }
  }, [query, applicationId, slug]);

  return !eventApplication ? (
    <LoadingIndicator isLoading={true} />
  ) : (
    <div className="flex flex-col space-y-5">
      <EventApplication
        application={eventApplication}
        slug={slug}
        showAnswersPage={false}
        approveCallback={setApproved}
      />
      <h1 className="text-xl">Answers</h1>
      <div className="flex flex-col ml-8 space-y-5">
        {eventApplication.questions.map((question) => (
          <Question
            key={question.id}
            title={question.title}
            description={question.description}
            isRequired={question.required}
            type={question.type}
            answer={question.answer}
          />
        ))}
      </div>
    </div>
  );
};

export default DisplayAnswersPage;
